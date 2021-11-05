# Sample of Seata XA mode

Spring Cloud 中使用 Seata，使用 Feign 实现远程调用，使用 Spring JDBC 访问 MySQL 数据库

### 准备工作

1. 执行`sql/all_in_one.sql`

2. 下载最新版本的 [Seata Sever](https://github.com/seata/seata/releases)

3. 解压并启动 Seata server

```bash
unzip seata-server-xxx.zip

cd distribution
sh ./bin/seata-server.sh 8091 file
```

4. 启动 AccountXA, OrderXA, StockXA, BusinessXA 服务

### 测试

- 无错误成功提交

```bash
curl http://127.0.0.1:8084/purchase
``` 

具体调用参数请结合 BusinessController 的代码。

数据初始化逻辑，参见 BusinessService#initData() 方法。

基于初始化数据，和默认的调用逻辑，purchase 将可以被成功调用 3 次。

每次账户余额扣减 3000，由最初的 10000 减少到 1000。

第 4 次调用，因为账户余额不足，purchase 调用将失败。相应的：库存、订单、账户都回滚。

### XA 模式与 AT 模式

只要切换数据源代理类型，该样例即可在 XA 模式和 AT 模式之间切换。

DataSourceConfiguration

XA 模式使用 DataSourceProxyXA

```java

public class DataSourceProxy {

    @Bean("dataSourceProxy")
    public DataSource dataSource(DruidDataSource druidDataSource) {
        // DataSourceProxyXA for XA mode
        return new DataSourceProxyXA(druidDataSource);
    }
}

```

AT 模式使用 DataSourceProxy

```java

public class DataSourceProxy {

    @Bean("dataSourceProxy")
    public DataSource dataSource(DruidDataSource druidDataSource) {
        // DataSourceProxyXA for AT mode
        return new DataSourceProxy(druidDataSource);
    }
}

```

*当然，AT 模式需要在数据库中建立 undo_log 表。（XA 模式是不需要这个表的）*

```sql
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

```

