# 基于 Seata 解决微服务架构下数据一致性的实践

[Seata](https://github.com/seata/seata) 是一款开源的分布式事务解决方案，提供高性能和简单易用的分布式事务服务。   



本文将通过一个简单的微服务架构的例子，说明业务如何step by step的使用 Seata、Dubbo 来保证业务数据的一致性；

本案例中，seata-server 服务端 使用数据库作为事务日志存储，用户可以部署多个seata-server以提供集群服务，从而支持服务端高可用。


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

### Seata、Dubbo 集成


#### Step 1 初始化 MySQL 数据库（需要InnoDB 存储引擎）

在 [resources/jdbc.properties](https://github.com/seata/seata-samples/blob/master/ha/src/main/resources/jdbc.properties) 修改StorageService、OrderService、AccountService 对应的连接信息。

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


相关建表脚本可在 [resources/sql/](https://github.com/seata/seata-samples/tree/master/ha/src/main/resources/sql) 下获取，在相应数据库中执行 [dubbo_biz.sql](https://github.com/seata/seata-samples/blob/master/ha/src/main/resources/sql/dubbo_biz.sql) 中的业务建表脚本，在每个数据库执行 [undo_log.sql](https://github.com/seata/seata-samples/blob/master/ha/src/main/resources/sql/undo_log.sql) 建表脚本。

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

#### Step 3 引入 Seata、Dubbo 相关 POM 依赖


```xml
      <properties>
          <seata.version>1.1.0</seata.version>
          <dubbo.alibaba.version>2.6.5</dubbo.alibaba.version>
       </properties>
        
       <dependency>
           <groupId>io.seata</groupId>
           <artifactId>seata-all</artifactId>
           <version>${seata.version}</version>
       </dependency>
      
       <dependency>
           <groupId>com.alibaba</groupId>
           <artifactId>dubbo</artifactId>
           <version>${dubbo.alibaba.version}</version>
       </dependency>
      
```


#### Step 4 微服务 Provider Spring配置

分别在三个微服务Spring配置文件（[dubbo-account-service.xml](https://github.com/seata/seata-samples/blob/master/ha/src/main/resources/spring/dubbo-account-service.xml)、
[dubbo-order-service](https://github.com/seata/seata-samples/blob/master/ha/src/main/resources/spring/dubbo-order-service.xml) 和 
[dubbo-storage-service.xml](https://github.com/seata/seata-samples/blob/master/ha/src/main/resources/spring/dubbo-storage-service.xml)
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
    <dubbo:registry address="zookeeper://localhost:2181" />
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

在 [dubbo-business.xml](https://github.com/seata/seata-samples/blob/master/ha/src/main/resources/spring/dubbo-business.xml) 配置以下配置：
- 配置 Dubbo 注册中心

同 Step 4

- 配置 Seata GlobalTransactionScanner

同 Step 4

- 在事务发起方 service 方法上添加 @GlobalTransactional 注解

```java
@GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
```
timeoutMills 为事务的总体超时时间默认60s，name 为事务方法签名的别名，默认为空。注解内参数均可省略。

#### Step 6 启动 Zookeeper

本地以单机模式启动 zookeeper-server;

#### Step 7 启动 Seata-Server

配置 Seata-Server的数据库，启动Seata-Server ；

- 下载 Seata-Server 最新 [release](https://github.com/seata/seata/releases) 包并解压

- 准备 Seata-Server的数据库：

安装mysql，创建数据库 seata_server : "create database seata_server;"

创建 事务记录表，建表语句 [db_store.sql](https://github.com/seata/seata/blob/develop/server/src/main/resources/db_store.sql)  :

```sql
-- the table to store GlobalSession data
drop table `global_table`;
create table `global_table` (
  `xid` varchar(128)  not null,
  `transaction_id` bigint,
  `status` tinyint not null,
  `application_id` varchar(64),
  `transaction_service_group` varchar(64),
  `transaction_name` varchar(128),
  `timeout` int,
  `begin_time` bigint,
  `application_data` varchar(2000),
  `gmt_create` datetime,
  `gmt_modified` datetime,
  primary key (`xid`),
  key `idx_gmt_modified_status` (`gmt_modified`, `status`),
  key `idx_transaction_id` (`transaction_id`)
);

-- the table to store BranchSession data
drop table `branch_table`;
create table `branch_table` (
  `branch_id` bigint not null,
  `xid` varchar(128) not null,
  `transaction_id` bigint ,
  `resource_group_id` varchar(128),
  `resource_id` varchar(256) ,
  `lock_key` varchar(256) ,
  `branch_type` varchar(8) ,
  `status` tinyint,
  `client_id` varchar(64),
  `application_data` varchar(2000),
  `gmt_create` datetime,
  `gmt_modified` datetime,
  primary key (`branch_id`),
  key `idx_xid` (`xid`)
);

-- the table to store lock data
drop table `lock_table`;
create table `lock_table` (
  `row_key` varchar(128) not null,
  `xid` varchar(128),
  `transaction_id` long ,
  `branch_id` long,
  `resource_id` varchar(256) ,
  `table_name` varchar(64) ,
  `pk` varchar(128) ,
  `gmt_create` datetime ,
  `gmt_modified` datetime,
  primary key(`row_key`)
);

```

- 初始化 Seata 配置

进入到 Seata-Server 解压目录 conf 文件夹下 [file.conf](https://github.com/seata/seata/blob/develop/server/src/main/resources/file.conf) ,修改事务日志存储相关属性：

修改项如下：
```
store.mode = "db"
store.db.datasource=dbcp
store.db.db-type=mysql
store.db.driver-class-name=com.mysql.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/seata_server?useUnicode=true
store.db.user=mysql
store.db.password=mysql
lock.mode=db

```

修改后 file.conf 中 store和lock 节点内容如下：

```properties
## transaction log store
store {
  ## store mode: file、db
  mode = "db"

  ## file store
  file {
    dir = "sessionStore"

    # branch session size , if exceeded first try compress lockkey, still exceeded throws exceptions
    max-branch-session-size = 16384
    # globe session size , if exceeded throws exceptions
    max-global-session-size = 512
    # file buffer size , if exceeded allocate new buffer
    file-write-buffer-cache-size = 16384
    # when recover batch read size
    session.reload.read_size = 100
    # async, sync
    flush-disk-mode = async
  }

  ## database store
  db {
      ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp) etc.
      datasource = "dbcp"
      ## mysql/oracle/h2/oceanbase etc.
      db-type = "mysql"
      driver-class-name = com.mysql.jdbc.Driver
      url = "jdbc:mysql://127.0.0.1:3306/seata_server"
      user = "mysql"
      password = "mysql"
      min-conn = 1
      max-conn = 3
      global.table = "global_table"
      branch.table = "branch_table"
      query-limit = 100
    }
}
lock {
    ## the data row lock store mode: local_db、memory or db
    mode = "db"

    memory{
         ## store lock in memory of server
    }

    db{
        ## use db of server to store lock, the db is ${store.db.url}
        lock-table= "lock_table"
    }

}


```   


- 运行 Seata-server

**Linux/Unix/Mac**

```bash
sh seata-server.sh $LISTEN_PORT $STORE_MODE $IP(此参数可选)
```

**Windows**

```bash
cmd seata-server.bat $LISTEN_PORT $PATH_FOR_PERSISTENT_DATA $IP(此参数可选)

```

**$LISTEN_PORT**: Seata-Server 服务端口      
**$STORE_MODE**: 事务操作记录存储模式：file、db  
**$IP(可选参数)**: 用于多 IP 环境下指定 Seata-Server 注册服务的IP      

**eg**:
sh seata-server.sh 8091 db

#### Step 8 启动微服务并测试

- 修改业务客户端发现注册方式为 zookeeper   
同Step 7 中[修改 Seata-server 服务注册方式为 zookeeper] 步骤
- 启动 [DubboAccountServiceStarter](https://github.com/seata/seata-samples/blob/master/ha/src/main/java/io/seata/samples/ha/starter/DubboAccountServiceStarter.java)
- 启动 [DubboOrderServiceStarter](https://github.com/seata/seata-samples/blob/master/ha/src/main/java/io/seata/samples/ha/starter/DubboOrderServiceStarter.java)
- 启动 [DubboStorageServiceStarter](https://github.com/seata/seata-samples/blob/master/ha/src/main/java/io/seata/samples/ha/starter/DubboStorageServiceStarter.java)

- 启动 [DubboBusinessTester](https://github.com/seata/seata-samples/blob/master/ha/src/main/java/io/seata/samples/ha/starter/DubboBusinessTester.java) 进行测试

**注意:** 在标注 @GlobalTransactional 注解方法内部显示的抛出异常才会进行事务的回滚。整个 Dubbo 服务调用链路只需要在事务最开始发起方的 service 方法标注注解即可。


## 相关链接:

本文 sample 地址: https://github.com/seata/seata-samples/tree/master/ha   
Seata: https://github.com/seata/seata   
Dubbo: https://github.com/apache/incubator-dubbo   





