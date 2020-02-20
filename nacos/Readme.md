# 基于 Seata 解决微服务架构下数据一致性的实践

[Seata](https://github.com/seata/seata) 是一款开源的分布式事务解决方案，提供高性能和简单易用的分布式事务服务。   
  

随着业务的快速发展，应用单体架构暴露出代码可维护性差，容错率低，测试难度大，敏捷交付能力差等诸多问题，微服务应运而生。微服务的诞生一方面解决了上述问题，但是另一方面却引入新的问题，其中主要问题之一就是如何保证微服务间的业务数据一致性。

本文将通过一个简单的微服务架构的例子，说明业务如何step by step的使用 Seata、Dubbo 和 Nacos 来保证业务数据的一致性。本文所述的例子中 Dubbo 和 Seata 注册配置服务中心均使用 Nacos。Seata 0.2.1+ 开始支持 Nacos 注册配置服务中心。


## 业务案例

用户采购商品业务，整个业务包含3个微服务:

- 库存服务: 扣减给定商品的库存数量。
- 订单服务: 根据采购请求生成订单。
- 账户服务: 用户账户金额扣减。

### 业务结构图

<img src="https://github.com/seata/seata-samples/blob/master/doc/img/fescar-1.png"  height="300" width="600">


#### StorageService

```java
public interface StorageService {
    /**
     * deduct storage count
     */
    void deduct(String commodityCode, int count);
}
```

#### OrderService

```java
public interface OrderService {
    /**
     * create order
     */
    Order create(String userId, String commodityCode, int orderCount);
}
```

#### AccountService

```java
public interface AccountService {
    /**
     * debit balance of user's account
     */
    void debit(String userId, int money);
}
```
**说明:** 以上三个微服务独立部署。

### Seata、Dubbo和Nacos 集成


#### Step 1 初始化 MySQL 数据库（需要InnoDB 存储引擎）

在 [resources/jdbc.properties](https://github.com/seata/seata-samples/blob/master/nacos/src/main/resources/jdbc.properties) 修改StorageService、OrderService、AccountService 对应的连接信息。

```properties
jdbc.account.url=jdbc:mysql://xxxx/xxxx
jdbc.account.username=xxxx
jdbc.account.password=xxxx
jdbc.account.driver=com.mysql.jdbc.Driver
# storage db config
jdbc.storage.url=jdbc:mysql://xxxx/xxxx
jdbc.storage.username=xxxx
jdbc.storage.password=xxxx
jdbc.storage.driver=com.mysql.jdbc.Driver
# order db config
jdbc.order.url=jdbc:mysql://xxxx/xxxx
jdbc.order.username=xxxx
jdbc.order.password=xxxx
jdbc.order.driver=com.mysql.jdbc.Driver
```

#### Step 2 创建 undo_log（用于 Seata AT 模式）表和相关业务表   


相关建表脚本可在 [resources/sql/](https://github.com/seata/seata-samples/tree/master/nacos/src/main/resources/sql) 下获取，在相应数据库中执行 [dubbo_biz.sql](https://github.com/seata/seata-samples/blob/master/nacos/src/main/resources/sql/dubbo_biz.sql) 中的业务建表脚本，在每个数据库执行 [undo_log.sql](https://github.com/seata/seata-samples/blob/master/nacos/src/main/resources/sql/undo_log.sql) 建表脚本。

```sql
-- 注意此处0.3.0+ 增加唯一索引 ux_undo_log
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

```sql
DROP TABLE IF EXISTS `storage_tbl`;
CREATE TABLE `storage_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commodity_code` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`commodity_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `order_tbl`;
CREATE TABLE `order_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
  `commodity_code` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT 0,
  `money` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `account_tbl`;
CREATE TABLE `account_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
  `money` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
**说明:** 需要保证每个物理库都包含 undo_log 表，此处可使用一个物理库来表示上述三个微服务对应的独立逻辑库。

#### Step 3 引入 Seata、Dubbo 和 Nacos 相关 POM 依赖


```xml
      <properties>
          <seata.version>1.1.0</seata.version>
          <nacos-client.version>1.0.0</nacos-client.version>
          <dubbo.alibaba.version>2.6.5</dubbo.alibaba.version>
          <dubbo.registry.nacos.version>0.0.2</dubbo.registry.nacos.version>
       </properties>
        
       <dependency>
           <groupId>io.seata</groupId>
           <artifactId>seata-all</artifactId>
           <version>${seata.version}</version>
       </dependency>
       <dependency>
           <groupId>com.alibaba.nacos</groupId>
           <artifactId>nacos-client</artifactId>
           <version>${nacos-client.version}</version>
       </dependency>
       <dependency>
           <groupId>com.alibaba</groupId>
           <artifactId>dubbo</artifactId>
           <version>${dubbo.alibaba.version}</version>
       </dependency>
       <dependency>
           <groupId>com.alibaba</groupId>
           <artifactId>dubbo-registry-nacos</artifactId>
           <version>${dubbo.registry.nacos.version}</version>
       </dependency>
```
**说明:** 由于当前 apache-dubbo 与 dubbo-registry-nacos jar存在兼容性问题，需要手动引入 alibaba-dubbo，后续 apache-dubbo(2.7.1+) 将兼容 dubbo-registry-nacos。在 Seata 中 seata-dubbo jar 支持 apache.dubbo，seata-dubbo-alibaba jar 支持 alibaba-dubbo。


#### Step 4 微服务 Provider Spring配置

分别在三个微服务Spring配置文件（[dubbo-account-service.xml](https://github.com/seata/seata-samples/blob/master/nacos/src/main/resources/spring/dubbo-account-service.xml)、
[dubbo-order-service](https://github.com/seata/seata-samples/blob/master/nacos/src/main/resources/spring/dubbo-order-service.xml) 和 
[dubbo-storage-service.xml](https://github.com/seata/seata-samples/blob/master/nacos/src/main/resources/spring/dubbo-storage-service.xml)
）进行如下配置：

- 配置 Seata 代理数据源

```xml
    <bean id="accountDataSourceProxy" class="io.seata.rm.datasource.DataSourceProxy">
        <constructor-arg ref="accountDataSource"/>
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="accountDataSourceProxy"/>
    </bean>
```

此处需要使用 io.seata.rm.datasource.DataSourceProxy 包装 Druid 数据源作为直接业务数据源，DataSourceProxy 用于业务 sql 的拦截解析并与 TC 交互协调事务操作状态。

- 配置 Dubbo 注册中心

```xml
    <dubbo:registry address="nacos://${nacos-server-ip}:8848"/>
```

- 配置 Seata GlobalTransactionScanner

```xml
    <bean class="io.seata.spring.annotation.GlobalTransactionScanner">
        <constructor-arg value="dubbo-demo-account-service"/>
        <constructor-arg value="my_test_tx_group"/>
    </bean>
```
此处构造方法的第一个参数为业务自定义 applicationId，若在单机部署多微服务需要保证 applicationId 唯一。   
构造方法的第二个参数为 Seata 事务服务逻辑分组，此分组通过配置中心配置项 service.vgroup_mapping.my_test_tx_group 映射到相应的 Seata-Server 集群名称，然后再根据集群名称.grouplist 获取到可用服务列表。

#### Step 5 事务发起方配置

在 [dubbo-business.xml](https://github.com/seata/seata-samples/blob/master/nacos/src/main/resources/spring/dubbo-business.xml) 配置以下配置：
- 配置 Dubbo 注册中心

同 Step 4

- 配置 Seata GlobalTransactionScanner

同 Step 4

- 在事务发起方 service 方法上添加 @GlobalTransactional 注解

```java
@GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
```
timeoutMills 为事务的总体超时时间默认60s，name 为事务方法签名的别名，默认为空。注解内参数均可省略。

#### Step 6 启动 Nacos-Server

- 下载 Nacos-Server 最新 [release](https://github.com/alibaba/nacos/releases) 包并解压

- 运行 Nacos-server

**Linux/Unix/Mac**

```bash
sh startup.sh -m standalone
```
**Windows**

```bash
cmd startup.cmd -m standalone
```

访问 Nacos 控制台：http://localhost:8848/nacos/index.html#/configurationManagement?dataId=&group=&appName=&namespace=

若访问成功说明 Nacos-Server 服务运行成功（默认账号/密码: nacos/nacos）


#### Step 7 启动 Seata-Server

- 下载 Seata-Server 最新 [release](https://github.com/seata/seata/releases) 包并解压

- 初始化 Seata 配置

进入到 Seata-Server 解压目录 conf 文件夹下，确认 [nacos-config.txt](https://github.com/seata/seata/blob/develop/server/src/main/resources/nacos-config.txt) 的配置值（一般不需要修改），确认完成后运行 [nacos-config.sh](https://github.com/seata/seata/blob/develop/server/src/main/resources/nacos-config.sh) 脚本初始化配置。

```bash
sh nacos-config.sh $Nacos-Server-IP
```
**eg**:

```bash

sh nacos-config.sh localhost 

```  

脚本执行最后输出 "**init nacos config finished, please start Seata-server.**" 说明推送配置成功。若想进一步确认可登陆Nacos 控制台 配置列表 筛选 Group=SEATA_GROUP 的配置项。

<img src="https://github.com/seata/seata-samples/blob/master/doc/img/nacos-1.png"  height="300" width="800">

- 修改 Seata-server 服务注册方式为 nacos

进入到 Seata-Server 解压目录 conf 文件夹下 [registry.conf](https://github.com/seata/seata/blob/develop/server/src/main/resources/registry.conf) 修改 type="nacos" 并配置 Nacos 的相关属性。

```properties
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "nacos"

  nacos {
    serverAddr = "localhost"
    namespace = ""
    cluster = "default"
  }
}

config {
  # file、nacos 、apollo、zk
  type = "nacos"

  nacos {
    serverAddr = "localhost"
    namespace = ""
  }
}


```
**type**: 可配置为注释中的类型，此处选择nacos类型，配置为 file 时无服务注册功能   
**nacos.serverAddr**: Nacos-Sever 服务地址(不含端口号)   
**nacos.namespace**: Nacos 注册和配置隔离 namespace   
**nacos.cluster**: 注册服务的集群名称   


- 运行 Seata-server

**Linux/Unix/Mac**

```bash
sh seata-server.sh -p $LISTEN_PORT -m $STORE_MODE -h $IP(此参数可选)
```

**Windows**

```bash
cmd seata-server.bat -p $LISTEN_PORT -m $STORE_MODE -h $IP(此参数可选)

```

**$LISTEN_PORT**: Seata-Server 服务端口      
**$STORE_MODE**: 事务操作记录存储模式：file、db  
**$IP(可选参数)**: 用于多 IP 环境下指定 Seata-Server 注册服务的IP      

**eg**:
sh seata-server.sh -p 8091 -m file

运行成功后可在 Nacos 控制台看到 服务名 =serverAddr 服务注册列表:

<img src="https://github.com/seata/seata-samples/blob/master/doc/img/nacos-2.png"  height="300" width="800">

#### Step 8 启动微服务并测试

- 修改业务客户端发现注册方式为 nacos   
同Step 7 中[修改 Seata-server 服务注册方式为 nacos] 步骤
- 启动 [DubboAccountServiceStarter](https://github.com/seata/seata-samples/blob/master/nacos/src/main/java/io/seata/samples/nacos/starter/DubboAccountServiceStarter.java)
- 启动 [DubboOrderServiceStarter](https://github.com/seata/seata-samples/blob/master/nacos/src/main/java/io/seata/samples/nacos/starter/DubboOrderServiceStarter.java)
- 启动 [DubboStorageServiceStarter](https://github.com/seata/seata-samples/blob/master/nacos/src/main/java/io/seata/samples/nacos/starter/DubboStorageServiceStarter.java)

启动完成可在 Nacos 控制台服务列表 看到启动完成的三个 provider

<img src="https://github.com/seata/seata-samples/blob/master/doc/img/nacos-3.png"  height="300" width="800">


- 启动 [DubboBusinessTester](https://github.com/seata/seata-samples/blob/master/nacos/src/main/java/io/seata/samples/nacos/starter/DubboBusinessTester.java) 进行测试

**注意:** 在标注 @GlobalTransactional 注解方法内部显示的抛出异常才会进行事务的回滚。整个 Dubbo 服务调用链路只需要在事务最开始发起方的 service 方法标注注解即可。


## 相关链接:

本文 sample 地址: https://github.com/seata/seata-samples/tree/master/nacos   
Seata: https://github.com/seata/seata   
Dubbo: https://github.com/apache/incubator-dubbo   
Nacos: https://github.com/alibaba/nacos





