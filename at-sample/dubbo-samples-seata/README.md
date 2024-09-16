
在Dubbo中可以使用Seata来实现分布式事务功能

## 开始之前
Apache Seata 是一款开源的分布式事务解决方案，致力于在微服务架构下提供高性能和简单易用的分布式事务服务。
在Dubbo中集成Seata实现分布式事务非常方便，只需简单几步即可完成，本文将带你快速体验。开始前，请先完成以下内容:
- 请克隆[seata-samples](https://github.com/apache/incubator-seata-samples)至本地并导入到开发工具中，并找到/at-sample/dubbo-samples-seata子项目。
- 请下载最新版的[seata-server二进制包](https://seata.apache.org/zh-cn/unversioned/download/seata-server)至本地。


### 步骤 1：建立数据库并初始化相关测试数据
- 本文将使用MySQL 5.7 (更多支持的数据库可在文末查看附录)。
  进入dubbo-samples-seata的script目录，找到dubbo_biz.sql和undo_log.sql两个数据库脚本文件，内容如下:

undo_log.sql是Seata AT 模式需要 `UNDO_LOG` 表
```sql
-- for AT mode you must to init this sql for you business database. the seata server not need it.
CREATE TABLE IF NOT EXISTS `undo_log`
(
    `branch_id`     BIGINT       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='AT transaction mode undo table';
ALTER TABLE `undo_log` ADD INDEX `ix_log_created` (`log_created`);

```
dubbo_biz.sql是示例业务表以及初始化数据

```sql
DROP TABLE IF EXISTS `stock_tbl`;
CREATE TABLE `stock_tbl`
(
    `id`             int(11) NOT NULL AUTO_INCREMENT,
    `commodity_code` varchar(255) DEFAULT NULL,
    `count`          int(11) DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`commodity_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `order_tbl`;
CREATE TABLE `order_tbl`
(
    `id`             int(11) NOT NULL AUTO_INCREMENT,
    `user_id`        varchar(255) DEFAULT NULL,
    `commodity_code` varchar(255) DEFAULT NULL,
    `count`          int(11) DEFAULT 0,
    `money`          int(11) DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `account_tbl`;
CREATE TABLE `account_tbl`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `user_id` varchar(255) DEFAULT NULL,
    `money`   int(11) DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

---INITIALIZE THE ACCOUNT TABLE
INSERT INTO account_tbl(`user_id`,`money`) VALUES('ACC_001','1000');
---INITIALIZE THE STOCK TABLE
INSERT INTO stock_tbl(`commodity_code`,`count`) VALUES('STOCK_001','100');

```
#### 请依次执行以下操作:
* 1.1 创建seata数据库(实际业务场景中会使用不同的数据库，本文为了方便演示仅创建一个数据库，所有的表都在该数据库中创建)
* 1.2 执行undo_log.sql表中的脚本完成AT模式所需的undo_log表创建
* 1.3 执行dubbo_biz.sql表中的脚本完成示例业务表创建以及测试数据的初始化

### 步骤 2：更新spring-boot应用配置中的数据库连接信息

请将以下3个子模块的数据库连接信息更新为你的信息，其他配置无需更改，至此，客户端的配置已经完毕。

* dubbo-samples-seata-account
* dubbo-samples-seata-order
* dubbo-samples-seata-stock
```yaml
url: jdbc:mysql://127.0.0.1:3306/seata?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useOldAliasMetadataBehavior=true
username: root
password: 123456
```

### 步骤 3：启动Seata-Server
- 本文使用的是Seata-Server V2.1.0版本。

请将下载的Seata-Server二进制包解压，并进入bin目录，然后执行以下命令即可启动Seata-Server。

如果你是Mac OS 或者Linux操作系统，请执行:
```
./seata-server.sh
```
或者你是Windows操作系统，请执行:
```
./seata-server.bat
```

### 步骤 4：启动示例

一切准备就绪，开始启动示例

#### 请依次启动以下子项目:
* 4.1 Account Service
* 4.2 Order Service
* 4.3 Stock Service
* 4.4 Business Service

### 步骤 5：查看分布式事务执行结果
通过访问以下链接，可以测试分布式事务成功提交流程:

http://127.0.0.1:9999/test/commit?userId=ACC_001&commodityCode=STOCK_001&orderCount=1

**分布式事务成功提交时，业务表的数据将正常更新，请注意观察数据库表中的数据。**

通过访问以下链接，可以测试分布式事务失败回滚流程:

http://127.0.0.1:9999/test/rollback?userId=ACC_001&commodityCode=STOCK_001&orderCount=1

**分布式事务失败回滚时，业务表的数据将没有任何改变，请注意观察数据库表中的数据。**

### 附录
* 支持的事务模式:Seata目前支持AT、TCC、SAGA、XA等模式，详情请访问[Seata官网](https://seata.apache.org/zh-cn/docs/user/mode/at)进行了解
* 支持的配置中心:Seata支持丰富的配置中心，如zookeeper、nacos、consul、apollo、etcd、file(本文使用此配置中心，无需第三方依赖，方便快速演示)，详情请访问[Seata配置中心](https://seata.apache.org/zh-cn/docs/user/configuration/)进行了解
* 支持的注册中心:Seata支持丰富的注册中心，如eureka、sofa、redis、zookeeper、nacos、consul、etcd、file(本文使用此注册中心，无需第三方依赖，方便快速演示)，详情请访问[Seata注册中心](https://seata.apache.org/zh-cn/docs/user/registry/)进行了解
* 支持的部署方式:直接部署、Docker、K8S、Helm等部署方式，详情请访问[Seata部署方式](https://seata.apache.org/zh-cn/docs/ops/deploy-guide-beginner)进行了解
* 支持的API:Seata的API分为两大类：High-Level API 和 Low-Level API，详情请访问[Seata API](https://seata.apache.org/zh-cn/docs/user/api)进行了解
* 支持的数据库:Seata支持MySQL、Oracle、PostgreSQL、TiDB、MariaDB等数据库，不同的事务模式会有差别，详情请访问[Seata支持的数据库](https://seata.apache.org/zh-cn/docs/user/datasource)进行了解
* 支持ORM框架:Seata 虽然是保证数据一致性的组件，但对于 ORM 框架并没有特殊的要求，像主流的Mybatis，Mybatis-Plus，Spring Data JPA, Hibernate等都支持。这是因为ORM框架位于JDBC结构的上层，而 Seata 的 AT,XA 事务模式是对 JDBC 标准接口操作的拦截和增强。详情请访问[Seata支持的ORM框架](https://seata.apache.org/zh-cn/docs/user/ormframework)进行了解
* 支持的微服务框架:Seata目前支持Dubbo、gRPC、hsf、http、motan、sofa等框架，同时seata提供了丰富的拓展机制，理论上可以支持任何微服务框架。详情请访问[Seata支持的微服务框架](https://seata.apache.org/zh-cn/docs/user/microservice)进行了解
* SQL限制:Seata 事务目前支持 INSERT、UPDATE、DELETE 三类 DML 语法的部分功能，这些类型都是已经经过Seata开源社区的验证。SQL 的支持范围还在不断扩大，建议在本文限制的范围内使用。详情请访问[Seata SQL限制](https://seata.apache.org/zh-cn/docs/user/sqlreference/sql-restrictions)进行了解
