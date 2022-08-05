# SEATA整合sharding-jdbc思路

​ 这里主要是参考了ShardingSphere官网的整合思路

​
参考连接：https://shardingsphere.apache.org/document/legacy/4.x/document/cn/features/transaction/principle/base-transaction-seata/

​ 具体内容如下：

​ 整合`Seata AT`事务时，需要把TM，RM，TC的模型融入到ShardingSphere
分布式事务的SPI的生态中。在数据库资源上，Seata通过对接DataSource接口，让JDBC操作可以同TC进行RPC通信。同样，ShardingSphere也是面向DataSource接口对用户配置的物理DataSource进行了聚合，因此把物理DataSource二次包装为Seata
的DataSource后，就可以把Seata AT事务融入到ShardingSphere的分片中。

​ 解读：

​ 这里注意ShardingSphere分布式事务的SPI的生态中，已经提供了整合的实现类SeataATShardingTransactionManager，只需要导入对应jar包就可以使用了

```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-transaction-base-seata-at</artifactId>
    <version>demo里面用的4.1.1版本</version>
</dependency>
```

​ 我们来看下SeataATShardingTransactionManager里面的内容：

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.shardingsphere.transaction.base.seata.at;

import com.google.common.base.Preconditions;
import io.seata.config.FileConfiguration;
import io.seata.core.context.RootContext;
import io.seata.core.rpc.netty.RmRpcClient;
import io.seata.core.rpc.netty.TmRpcClient;
import io.seata.rm.RMClient;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.tm.TMClient;
import io.seata.tm.api.GlobalTransactionContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.shardingsphere.spi.database.DatabaseType;
import org.apache.shardingsphere.transaction.core.ResourceDataSource;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.spi.ShardingTransactionManager;

public final class SeataATShardingTransactionManager implements ShardingTransactionManager {
    private final Map<String, DataSource> dataSourceMap = new HashMap();
    private final FileConfiguration configuration = new FileConfiguration("seata.conf");

    public SeataATShardingTransactionManager() {
    }

    public void init(DatabaseType databaseType, Collection<ResourceDataSource> resourceDataSources) {
        this.initSeataRPCClient();
        Iterator var3 = resourceDataSources.iterator();

        while(var3.hasNext()) {
            ResourceDataSource each = (ResourceDataSource)var3.next();
            this.dataSourceMap.put(each.getOriginalName(), new DataSourceProxy(each.getDataSource()));
        }

    }

    public TransactionType getTransactionType() {
        return TransactionType.BASE;
    }

    public boolean isInTransaction() {
        return null != RootContext.getXID();
    }

    public Connection getConnection(String dataSourceName) throws SQLException {
        return ((DataSource)this.dataSourceMap.get(dataSourceName)).getConnection();
    }

    public void begin() {
        try {
            SeataTransactionHolder.set(GlobalTransactionContext.getCurrentOrCreate());
            SeataTransactionHolder.get().begin();
            SeataTransactionBroadcaster.collectGlobalTxId();
        } catch (Throwable var2) {
            throw var2;
        }
    }

    public void commit() {
        try {
            try {
                SeataTransactionHolder.get().commit();
            } finally {
                SeataTransactionBroadcaster.clear();
                SeataTransactionHolder.clear();
            }

        } catch (Throwable var5) {
            throw var5;
        }
    }

    public void rollback() {
        try {
            try {
                SeataTransactionHolder.get().rollback();
            } finally {
                SeataTransactionBroadcaster.clear();
                SeataTransactionHolder.clear();
            }

        } catch (Throwable var5) {
            throw var5;
        }
    }

    public void close() {
        this.dataSourceMap.clear();
        SeataTransactionHolder.clear();
        TmRpcClient.getInstance().destroy();
        RmRpcClient.getInstance().destroy();
    }

    private void initSeataRPCClient() {
        String applicationId = this.configuration.getConfig("client.application.id");
        Preconditions.checkNotNull(applicationId, "please config application id within seata.conf file");
        String transactionServiceGroup = this.configuration.getConfig("client.transaction.service.group", "default");
        TMClient.init(applicationId, transactionServiceGroup);
        RMClient.init(applicationId, transactionServiceGroup);
    }
}
```

从源码得知SeataATShardingTransactionManager主要做了三件事：

第一：将所有数据源封装成seata的代理数据源DataSourceProxy，并放入dataSourceMap中备用。

第二：初始化TM和RM。

第三：提供开启，提交，回滚和关闭事务的方法。

看到这三个功能，是不是很像seata中GlobalTransactionScanner的作用？

没错！

这二者的功能是一模一样的，说白了，这里ShardingSphere就是拿自己分库分表的配置文件做里子，用seata的提供好的DataSourceProxy，TMClient，RMClient和GlobalTransaction这几个对象做衣服，重新封装了一个控制分布式事务的对象。

说人话，就是SeataATShardingTransactionManager对GlobalTransactionScanner进行了二次封装，不仅满足单库微服务的分布式事务，还满足分库分表微服务的分布式事务，变得更强了。

那么如何使用SeataATShardingTransactionManager呢？

非常简单，两步搞定！

第一：在resources下提供seata.conf配置文件，内容如下：

```
client {
    application.id = order-server
    transaction.service.group = ltf_tx_group
}
```

第二：用@Transactional搭配@ShardingTransactionType(TransactionType.BASE)来开启全局事务

```java
//这里切记不要加@GlobalTransactional
@Transactional
@ShardingTransactionType(TransactionType.BASE)
public void seataDemo(Boolean hasError) {
    //下单操作
    Order order = new Order();
    order.setOrderName("测试数据");
    order.setBuyNum(2);
    orderMapper.insert(order);

    //减库存（这里参数什么的就自己脑补了）
    productClient.minusStock();

    //异常模拟
    if(hasError){
        int i=1/0;
    }
}
```

但是这里要注意一个问题！！！！！！

**虽然SeataATShardingTransactionManager和GlobalTransactionScanner做的事情一样，但是二者绝对不能混合使用！！！！！！**

**也就是说@ShardingTransactionType(TransactionType.BASE)和@GlobalTransactional绝对不能同时出现！！！！！！**

看过上面SeataATShardingTransactionManager你就能明白，ShardingSphere把获取到的全局事务，放到了线程的局部变量里面了。而GlobalTransactionScanner则是采用动态代理的方式对方法进行增强。根本不能放在一起，杂交是要出问题的！

ok！最终总结一句！

只要你的项目中用到sharding-jdbc，那么你全部微服务都需要导入seata整合sharding-jdbc的jar包

```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-transaction-base-seata-at</artifactId>
    <version>demo里面用的4.1.1版本</version>
</dependency>
```

而且，你的项目中千万不能再出现@GlobalTransactional注解了，用SeataATShardingTransactionManager就可以了。

# 项目启动流程

一、启动nacos（demo版本1.4.0），导入配置文件，配置文件在项目sql-and-seataconfig文件夹中。

二、启动seata（demo版本1.3.0，其实1.4.2也支持），将seata配置registry.conf中配置中心和注册中心都改成nacos。

三、启动demo。

​ POST访问http://localhost:8001/seata/test?hasError=false，订单和产品都成功，日志也有提交信息。

​ POST访问http://localhost:8001/seata/test?hasError=true，订单和产品都失败，日志也有回滚信息。