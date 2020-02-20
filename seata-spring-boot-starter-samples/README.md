## 1.简介
>本文主要介绍SpringBoot2.2.2 + Dubbo 2.7.5 + Mybatis 3.4.2 + Nacos 1.1.3 +Seata 1.0.0整合来实现Dubbo分布式事务管理，使用Nacos 作为 Dubbo和Seata的注册中心和配置中心,使用 MySQL 数据库和 MyBatis来操作数据。

如果你还对`SpringBoot`、`Dubbo`、`Nacos`、`Seata`、` Mybatis` 不是很了解的话，这里我为大家整理个它们的官网网站，如下

- SpringBoot：[https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)

 - Dubbo：[http://dubbo.apache.org/en-us/](http://dubbo.apache.org/en-us/)

- Nacos：[https://nacos.io/zh-cn/docs/quick-start.html](https://nacos.io/zh-cn/docs/quick-start.html)

- Seata：[https://seata.io/zh-cn/](https://seata.io/zh-cn/)

- MyBatis：[http://www.mybatis.org/mybatis-3/zh/index.html](http://www.mybatis.org/mybatis-3/zh/index.html)

在这里我们就不一个一个介绍它们是怎么使用和原理，详细请学习官方文档，在这里我将开始对它们进行整合，完成一个简单的案例，来让大家了解`Seata`来实现`Dubbo`分布式事务管理的基本流程。

## 2.环境准备
## 2.1 下载nacos并安装启动
nacos下载：[https://github.com/alibaba/nacos/releases/tag/1.1.3](https://github.com/alibaba/nacos/releases/tag/1.1.3)

Nacos 快速入门：[https://nacos.io/en-us/docs/quick-start.html](https://nacos.io/en-us/docs/quick-start.html)

```shell
sh startup.sh -m standalone
```

在浏览器打开Nacos web 控制台：http://192.168.10.200:8848/nacos/index.html

输入nacos的账号和密码 分别为`nacos：nacos`

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190905101221566.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
这是时候naocs 就正常启动了。
## 2.2 下载seata server 并安装启动

#### 2.2.1 在 [Seata Release](https://github.com/seata/seata/releases) 下载最新版的 Seata Server 并解压得到如下目录：


```shell
.
├──bin
├──conf
├──file_store
└──lib
```
#### 2.2.2 修改 conf/registry.conf 配置，
目前seata支持如下的file、nacos 、apollo、zk、consul的注册中心和配置中心。这里我们以`nacos` 为例。
将 type 改为 nacos

```bash
registry {
  # file nacos
  type = "nacos"

  nacos {
    serverAddr = "192.168.10.200:8848"
    namespace = ""
    cluster = "default"
  }
  file {
    name = "file.conf"
  }
}

config {
  # file、nacos 、apollo、zk、consul
  type = "nacos"

  nacos {
    serverAddr = "192.168.10.200:8848"
    namespace = ""
  }

  file {
    name = "file.conf"
  }
}
```
- serverAddr = "192.168.10.200:8848"   ：nacos 的地址
- namespace = "" ：nacos的命名空间默认为``
- cluster = "default"  ：集群设置未默认 `default`

**注意： seata0.9.0之后，配置如下, 其中`namespace = ""`**
#### 2.2.3 修改 conf/nacos-config.txt配置

```
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.thread-factory.boss-thread-prefix=NettyBoss
transport.thread-factory.worker-thread-prefix=NettyServerNIOWorker
transport.thread-factory.server-executor-thread-prefix=NettyServerBizHandler
transport.thread-factory.share-boss-worker=false
transport.thread-factory.client-selector-thread-prefix=NettyClientSelector
transport.thread-factory.client-selector-thread-size=1
transport.thread-factory.client-worker-thread-prefix=NettyClientWorkerThread
transport.thread-factory.boss-thread-size=1
transport.thread-factory.worker-thread-size=8
transport.shutdown.wait=3
service.vgroup_mapping.order-service-seata-service-group=default
service.vgroup_mapping.account-service-seata-service-group=default
service.vgroup_mapping.storage-service-seata-service-group=default
service.vgroup_mapping.business-service-seata-service-group=default
service.enableDegrade=false
service.disable=false
service.max.commit.retry.timeout=-1
service.max.rollback.retry.timeout=-1
client.async.commit.buffer.limit=10000
client.lock.retry.internal=10
client.lock.retry.times=30
store.mode=db
store.file.dir=file_store/data
store.file.max-branch-session-size=16384
store.file.max-global-session-size=512
store.file.file-write-buffer-cache-size=16384
store.file.flush-disk-mode=async
store.file.session.reload.read_size=100
store.db.driver-class-name=com.mysql.jdbc.Driver
store.db.datasource=dbcp
store.db.db-type=mysql
store.db.url=jdbc:mysql://192.168.10.200:3306/seata?useUnicode=true
store.db.user=lidong
store.db.password=cwj887766@@
store.db.min-conn=1
store.db.max-conn=3
store.db.global.table=global_table
store.db.branch.table=branch_table
store.db.query-limit=100
store.db.lock-table=lock_table
recovery.committing-retry-period=1000
recovery.asyn-committing-retry-period=1000
recovery.rollbacking-retry-period=1000
recovery.timeout-retry-period=1000
transaction.undo.data.validation=true
transaction.undo.log.serialization=jackson
transaction.undo.log.save.days=7
transaction.undo.log.delete.period=86400000
transaction.undo.log.table=undo_log
transport.serialization=seata
transport.compressor=none
metrics.enabled=false
metrics.registry-type=compact
metrics.exporter-list=prometheus
metrics.exporter-prometheus-port=9898
client.report.retry.count=5
service.disableGlobalTransaction=false
client.support.spring.datasource.autoproxy=true
```
配置的详细说明参考官网：[https://seata.io/zh-cn/docs/user/configurations.html](https://seata.io/zh-cn/docs/user/configurations.html)

这里主要修改了如下几项：
- store.mode :存储模式 默认file  这里我修改为db 模式 ，并且需要三个表`global_table`、`branch_table`和`lock_table`
- store.db.driver-class-name： 0.8.0版本默认没有，会报错。添加了 `com.mysql.jdbc.Driver`
- store.db.datasource=dbcp ：数据源 dbcp
- store.db.db-type=mysql : 存储数据库的类型为`mysql`
- store.db.url=jdbc:mysql://192.168.10.200:3306/seata?useUnicode=true : 修改为自己的数据库`url`、`port`、`数据库名称`
- store.db.user=lidong :数据库的账号
- store.db.password=cwj887766@@ :数据库的密码
- service.vgroup_mapping.order-service-seata-service-group=default
- service.vgroup_mapping.account-service-seata-service-group=default
- service.vgroup_mapping.storage-service-seata-service-group=default
- service.vgroup_mapping.business-service-seata-service-group=default
- client.support.spring.datasource.autoproxy=true 开启数据源自动代理

也可以在 Nacos 配置页面添加，data-id 为 service.vgroup_mapping.${YOUR_SERVICE_NAME}-seata-service-group, group 为 SEATA_GROUP， 如果不添加该配置，启动后会提示no available server to connect

**注意：** 配置文件末尾有空行，需要删除，否则会提示失败，尽管实际上是成功的

***db模式下的所需的三个表的数据库脚本位于`seata\conf\db_store.sql`***

`global_table`的表结构

```sql
CREATE TABLE `global_table` (
  `xid` varchar(128) NOT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `application_id` varchar(64) DEFAULT NULL,
  `transaction_service_group` varchar(64) DEFAULT NULL,
  `transaction_name` varchar(64) DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `begin_time` bigint(20) DEFAULT NULL,
  `application_data` varchar(2000) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`xid`),
  KEY `idx_gmt_modified_status` (`gmt_modified`,`status`),
  KEY `idx_transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

```

`branch_table`的表结构

```sql
CREATE TABLE `branch_table` (
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(128) NOT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  `resource_group_id` varchar(32) DEFAULT NULL,
  `resource_id` varchar(256) DEFAULT NULL,
  `lock_key` varchar(128) DEFAULT NULL,
  `branch_type` varchar(8) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `client_id` varchar(64) DEFAULT NULL,
  `application_data` varchar(2000) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`branch_id`),
  KEY `idx_xid` (`xid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


```
`lock_table`的表结构

```
create table `lock_table` (
  `row_key` varchar(128) not null,
  `xid` varchar(96),
  `transaction_id` long ,
  `branch_id` long,
  `resource_id` varchar(256) ,
  `table_name` varchar(32) ,
  `pk` varchar(32) ,
  `gmt_create` datetime ,
  `gmt_modified` datetime,
  primary key(`row_key`)
);
```

#### 2.2.4 将 Seata 配置添加到 Nacos 中
使用命令如下

```
cd conf
sh nacos-config.sh localhost
```

成功后会提示

```
init nacos config finished, please start seata-server
```

在 Nacos 管理页面应该可以看到有 62 个 Group 为SEATA_GROUP的配置 

![在这里插入图片描述](https://img-blog.csdnimg.cn/2019090510533734.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
#### 2.2.5 启动 Seata Server

使用db 模式启动

```shell
 cd ..
 sh ./bin/seata-server.sh
```
这时候在 Nacos 的服务列表下面可以看到一个名为serverAddr的服务
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190905110455278.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
## 3 使用seata-spring-boot-starter案例分析
`seata-spring-boot-starter`是使用springboot自动装配来简化seata-all的复杂配置。1.0.0可用于替换seata-all，`GlobalTransactionScanner`自动初始化（依赖SpringUtils）若其他途径实现`GlobalTransactionScanner`初始化，请保证`io.seata.spring.boot.autoconfigure.util.SpringUtils`先初始化；
`seata-spring-boot-starter`默认开启数据源自动代理，用户若再手动配置`DataSourceProxy`将会导致异常。


参考官网中用户购买商品的业务逻辑。整个业务逻辑由4个微服务提供支持：

- 库存服务：扣除给定商品的存储数量。
- 订单服务：根据购买请求创建订单。
- 帐户服务：借记用户帐户的余额。
- 业务服务：处理业务逻辑。

请求逻辑架构
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190905111031350.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)

## 3.1  github地址
springboot-dubbo-seata：[https://github.com/lidong1665/spring-cloud-learning-example/tree/master/springboot-dubbo-seata-nacos](https://github.com/lidong1665/spring-cloud-learning-example/tree/master/springboot-dubbo-seata-nacos)
- samples-common ：公共模块

- samples-account ：用户账号模块

- samples-order ：订单模块

- samples-storage ：库存模块

- samples-business ：业务模块


#### 3.2 账户服务：AccountDubboService

```java
/**
 * @Author: lidong
 * @Description  账户服务接口
 * @Date Created in 2019/9/5 16:37
 */
public interface AccountDubboService {

    /**
     * 从账户扣钱
     */
    ObjectResponse decreaseAccount(AccountDTO accountDTO);
}
```
#### 3.3 订单服务：OrderDubboService

```java
/**
 * @Author: lidong
 * @Description  订单服务接口
 * @Date Created in 2019/9/5 16:28
 */
public interface OrderDubboService {

    /**
     * 创建订单
     */
    ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO);
}
```
#### 3.4  库存服务：StorageDubboService

```java
/**
 * @Author: lidong
 * @Description  库存服务
 * @Date Created in 2019/9/5 16:22
 */
public interface StorageDubboService {

    /**
     * 扣减库存
     */
    ObjectResponse decreaseStorage(CommodityDTO commodityDTO);
}

```

#### 3.5 业务服务：BusinessService 

```java

/**
 * @Author: lidong
 * @Description
 * @Date Created in 2019/9/5 17:17
 */
public interface BusinessService {

    /**
     * 出处理业务服务
      * @param businessDTO
     * @return
     */
    ObjectResponse handleBusiness(BusinessDTO businessDTO);
}
```

业务逻辑的具体实现主要体现在 订单服务的实现和业务服务的实现

订单服务的实现
```java
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {

    @Reference(version = "1.0.0")
    private AccountDubboService accountDubboService;

    /**
     * 创建订单
     * @Param:  OrderDTO  订单对象
     * @Return:  OrderDTO  订单对象
     */
    @Override
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO) {
        ObjectResponse<OrderDTO> response = new ObjectResponse<>();
        //扣减用户账户
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(orderDTO.getUserId());
        accountDTO.setAmount(orderDTO.getOrderAmount());
        ObjectResponse objectResponse = accountDubboService.decreaseAccount(accountDTO);

        //生成订单号
        orderDTO.setOrderNo(UUID.randomUUID().toString().replace("-",""));
        //生成订单
        TOrder tOrder = new TOrder();
        BeanUtils.copyProperties(orderDTO,tOrder);
        tOrder.setCount(orderDTO.getOrderCount());
        tOrder.setAmount(orderDTO.getOrderAmount().doubleValue());
        try {
            baseMapper.createOrder(tOrder);
        } catch (Exception e) {
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }

        if (objectResponse.getStatus() != 200) {
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.SUCCESS.getCode());
        response.setMessage(RspStatusEnum.SUCCESS.getMessage());
        return response;
    }
}
```

整个业务的实现逻辑
```java
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService{

    @Reference(version = "1.0.0")
    private StorageDubboService storageDubboService;

    @Reference(version = "1.0.0")
    private OrderDubboService orderDubboService;

    private boolean flag;

    /**
     * 处理业务逻辑
     * @Param:
     * @Return:
     */

    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    @Override
    public ObjectResponse handleBusiness(BusinessDTO businessDTO) {
        log.info("开始全局事务，XID = " + RootContext.getXID());
        ObjectResponse<Object> objectResponse = new ObjectResponse<>();
        //1、扣减库存
        CommodityDTO commodityDTO = new CommodityDTO();
        commodityDTO.setCommodityCode(businessDTO.getCommodityCode());
        commodityDTO.setCount(businessDTO.getCount());
        ObjectResponse storageResponse = storageDubboService.decreaseStorage(commodityDTO);
        //2、创建订单
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(businessDTO.getUserId());
        orderDTO.setCommodityCode(businessDTO.getCommodityCode());
        orderDTO.setOrderCount(businessDTO.getCount());
        orderDTO.setOrderAmount(businessDTO.getAmount());
        ObjectResponse<OrderDTO> response = orderDubboService.createOrder(orderDTO);

        //打开注释测试事务发生异常后，全局回滚功能
//        if (!flag) {
//            throw new RuntimeException("测试抛异常后，分布式事务回滚！");
//        }

        if (storageResponse.getStatus() != 200 || response.getStatus() != 200) {
            throw new DefaultException(RspStatusEnum.FAIL);
        }

        objectResponse.setStatus(RspStatusEnum.SUCCESS.getCode());
        objectResponse.setMessage(RspStatusEnum.SUCCESS.getMessage());
        objectResponse.setData(response.getData());
        return objectResponse;
    }
}
```
## 3.6 使用seata的分布式事务解决方案处理dubbo的分布式事务
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190905113350848.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)

我们只需要在业务处理的方法`handleBusiness`添加一个注解 `@GlobalTransactional` 

```java
@GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    @Override
    public ObjectResponse handleBusiness(BusinessDTO businessDTO) {
    
    }
```
- `timeoutMills`: 超时时间
- `name ` ：事务名称

## 3.7 准备数据库
注意: MySQL必须使用`InnoDB engine`.

创建数据库  并导入数据库脚本
```sql
DROP DATABASE IF EXISTS seata;
CREATE DATABASE seata;
USE seata;

DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
  `amount` double(14,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_account
-- ----------------------------
INSERT INTO `t_account` VALUES ('1', '1', '4000.00');

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `commodity_code` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT '0',
  `amount` double(14,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order
-- ----------------------------

-- ----------------------------
-- Table structure for t_storage
-- ----------------------------
DROP TABLE IF EXISTS `t_storage`;
CREATE TABLE `t_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commodity_code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `commodity_code` (`commodity_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_storage
-- ----------------------------
INSERT INTO `t_storage` VALUES ('1', 'C201901140001', '水杯', '1000');

-- ----------------------------
-- Table structure for undo_log
-- 注意此处0.3.0+ 增加唯一索引 ux_undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
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

-- ----------------------------
-- Records of undo_log
-- ----------------------------
SET FOREIGN_KEY_CHECKS=1;
```

会看到如上的4个表

```shell
+-------------------------+
| Tables_in_seata         |
+-------------------------+
| t_account               |
| t_order                 |
| t_storage               |
| undo_log                |
+-------------------------+
```

这里为了简化我将这个三张表创建到一个库中,使用是三个数据源来实现。

## 3.8 我们以账号服务`samples-account`为例 ，分析需要注意的配置项目
### 3.8.1 引入的依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <artifactId>springboot-dubbo-seata</artifactId>
    <packaging>pom</packaging>
    <name>springboot-dubbo-seata</name>
    <groupId>io.seata</groupId>
    <version>1.1.0</version>
    <description>Demo project for Spring Cloud Alibaba Dubbo</description>

    <modules>
        <module>samples-common</module>
        <module>samples-account</module>
        <module>samples-order</module>
        <module>samples-storage</module>
        <module>samples-business</module>
    </modules>

    <properties>
        <springboot.verison>2.2.2.RELEASE</springboot.verison>
        <java.version>1.8</java.version>
        <druid.version>1.1.10</druid.version>
        <mybatis.version>1.3.2</mybatis.version>
        <mybatis-plus.version>2.3</mybatis-plus.version>
        <nacos.version>0.2.3</nacos.version>
        <lombok.version>1.16.22</lombok.version>
        <dubbo.version>2.7.5</dubbo.version>
        <nacos-client.verison>1.1.3</nacos-client.verison>
        <seata.version>1.1.0</seata.version>
        <netty.version>4.1.32.Final</netty.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${springboot.verison}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>${springboot.verison}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>${springboot.verison}</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>${druid.version}</version>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis.version}</version>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>

        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
            <exclusions>
                <exclusion>
                    <artifactId>spring</artifactId>
                    <groupId>org.springframework</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>${dubbo.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.apache.dubbo/dubbo-config-spring -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-config-spring</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-registry-nacos</artifactId>
            <version>${dubbo.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.seata/seata-all -->

        <dependency>
            <groupId>io.seata</groupId>
            <artifactId>seata-spring-boot-starter</artifactId>
            <version>${seata.version}</version>
        </dependency>


        <dependency>
            <groupId>com.alibaba.nacos</groupId>
            <artifactId>nacos-client</artifactId>
            <version>${nacos-client.verison}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>${springboot.verison}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>


        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>${netty.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba.spring</groupId>
            <artifactId>spring-context-support</artifactId>
            <version>1.0.5</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
    </dependencies>


    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-deploy-plugin</artifactId>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

```
注意：
- `seata-spring-boot-starter`: 这个是spring-boot seata 所需的主要依赖，1.0.0版本开始加入支持。
- `dubbo-spring-boot-starter`:   springboot dubbo的依赖

其他的就不一一介绍，其他的一目了然，就知道是干什么的。
  
 ### 3.8.2  application.yml配置
 

```yml
server:
  port: 8102
#====================================datasource =============================================
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/seata?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
    username: root
    password: 123456
  application:
    name: dubbo-account-example

#====================================Dubbo config===============================================
dubbo:
  application:
    id: dubbo-account-example
    name: dubbo-account-example
    qosEnable: false
  protocol:
    id: dubbo
    name: dubbo
    port: 20883
  registry:
    id: dubbo-account-example-registry
    address: nacos://127.0.0.1:8848
  config-center:
    address: nacos://127.0.0.1:8848
  metadata-report:
    address: nacos://127.0.0.1:8848
#====================================mybatis-plus config===============================================
mybatis-plus:
  mapper-locations: classpath*:/mapper/*.xml
  typeAliasesPackage: io.seata.samples.integration.*.entity
  configuration:
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      id-type: auto

#====================================Seata Config===============================================

seata:
  enabled: true
  application-id: account-seata-example
  tx-service-group: account-service-seata-service-group # 事务群组（可以每个应用独立取名，也可以使用相同的名字）
  client:
    rm-report-success-enable: true
    rm-table-meta-check-enable: false # 自动刷新缓存中的表结构（默认false）
    rm-report-retry-count: 5 # 一阶段结果上报TC重试次数（默认5）
    rm-async-commit-buffer-limit: 10000 # 异步提交缓存队列长度（默认10000）
    rm:
      lock:
        lock-retry-internal: 10 # 校验或占用全局锁重试间隔（默认10ms）
        lock-retry-times:    30 # 校验或占用全局锁重试次数（默认30）
        lock-retry-policy-branch-rollback-on-conflict: true # 分支事务与其它全局回滚事务冲突时锁策略（优先释放本地锁让回滚成功）
    tm-commit-retry-count:   3 # 一阶段全局提交结果上报TC重试次数（默认1次，建议大于1）
    tm-rollback-retry-count: 3 # 一阶段全局回滚结果上报TC重试次数（默认1次，建议大于1）
    undo:
      undo-data-validation: true # 二阶段回滚镜像校验（默认true开启）
      undo-log-serialization: jackson # undo序列化方式（默认jackson）
      undo-log-table: undo_log  # 自定义undo表名（默认undo_log）
    log:
      exceptionRate: 100 # 日志异常输出概率（默认100）
    support:
      spring:
        datasource-autoproxy: true
  service:
    vgroup-mapping: default # TC 集群（必须与seata-server保持一致）
    enable-degrade: false # 降级开关
    disable-global-transaction: false # 禁用全局事务（默认false）
    grouplist: 127.0.0.1:8091
  transport:
    shutdown:
      wait: 3
    thread-factory:
      boss-thread-prefix: NettyBoss
      worker-thread-prefix: NettyServerNIOWorker
      server-executor-thread-prefix: NettyServerBizHandler
      share-boss-worker: false
      client-selector-thread-prefix: NettyClientSelector
      client-selector-thread-size: 1
      client-worker-thread-prefix: NettyClientWorkerThread
    type: TCP
    server: NIO
    heartbeat: true
    serialization: seata
    compressor: none
    enable-client-batch-send-request: true # 客户端事务消息请求是否批量合并发送（默认true）
  registry:
    file:
      name: file.conf
    type: nacos
    nacos:
      server-addr: localhost:8848
      namespace:
      cluster: default
  config:
    file:
      name: file.conf
    type: nacos
    nacos:
      namespace:
      server-addr: localhost:8848
```
### 3.8.3 SeataDataSourceAutoConfig  配置

```java
package io.seata.samples.integration.account.config;

import com.alibaba.druid.pool.DruidDataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Author: lidong
 * @Description  seata global configuration
 * @Date Created in 2019/9/05 10:28
 */
@Configuration
public class SeataDataSourceAutoConfig {

    /**
     * autowired datasource config
     */
    @Autowired
    private DataSourceProperties dataSourceProperties;

    /**
     * init durid datasource
     *
     * @Return: druidDataSource  datasource instance
     */
    @Bean
    @Primary
    public DruidDataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        druidDataSource.setInitialSize(0);
        druidDataSource.setMaxActive(180);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(0);
        druidDataSource.setValidationQuery("Select 1 from DUAL");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(25200000);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(1800);
        druidDataSource.setLogAbandoned(true);
        return druidDataSource;
    }
    /**
     * init mybatis sqlSessionFactory
     * @Param: dataSourceProxy  datasource proxy
     * @Return: DataSourceProxy  datasource proxy
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mapper/*.xml"));
        return factoryBean.getObject();
    }
}

```
### 3.8.4 AccountExampleApplication 启动类的配置

```java
package io.seata.samples.integration.account;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;

@SpringBootApplication(scanBasePackages = "io.seata.samples.integration.account")
@MapperScan({"io.seata.samples.integration.account.mapper"})
@EnableDubbo(scanBasePackages = "io.seata.samples.integration.account")
public class AccountExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountExampleApplication.class, args);
    }

}


```

- `@EnableDubbo`等同于 `@DubboComponentScan`和 `@EnableDubboConfig`组合

 - `@DubboComponentScan` 扫描 classpaths 下类中添加了 `@Service` 和 `@Reference` 将自动注入到spring beans中。
 - @EnableDubboConfig 扫描的dubbo的外部化配置。

## 4 启动所有的sample模块
启动 `samples-account`、`samples-order`、`samples-storage`、`samples-business`

并且在nocos的控制台查看注册情况: http://192.168.10.200:8848/nacos/#/serviceManagement

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190905131449502.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)

我们可以看到上面的服务都已经注册成功。

## 5 测试
### 5. 1 发送一个下单请求
使用postman 发送 ：[http://localhost:8104/business/dubbo/buy](http://localhost:8104/business/dubbo/buy) 

参数：

```json
{
    "userId":"1",
    "commodityCode":"C201901140001",
    "name":"fan",
    "count":50,
    "amount":"100"
}
```
返回

```json
{
    "status": 200,
    "message": "成功",
    "data": null
}
```
这时候控制台：

```
2020-01-08 10:49:37.693  INFO 24032 --- [nio-8104-exec-2] i.s.s.i.c.controller.BusinessController  : 请求参数：BusinessDTO(userId=1, commodityCode=C201901140001, name=fan, count=50, amount=100)
2020-01-08 10:49:50.866  INFO 24032 --- [nio-8104-exec-2] i.s.common.loader.EnhancedServiceLoader  : load ContextCore[null] extension by class[io.seata.core.context.ThreadLocalContextCore]
2020-01-08 10:49:53.619  INFO 24032 --- [nio-8104-exec-2] i.s.common.loader.EnhancedServiceLoader  : load TransactionManager[null] extension by class[io.seata.tm.DefaultTransactionManager]
2020-01-08 10:49:53.620  INFO 24032 --- [nio-8104-exec-2] io.seata.tm.TransactionManagerHolder     : TransactionManager Singleton io.seata.tm.DefaultTransactionManager@17f4ccad
2020-01-08 10:49:55.553  INFO 24032 --- [nio-8104-exec-2] i.s.common.loader.EnhancedServiceLoader  : load LoadBalance[null] extension by class[io.seata.discovery.loadbalance.RandomLoadBalance]
2020-01-08 10:49:58.044  INFO 24032 --- [nio-8104-exec-2] i.seata.tm.api.DefaultGlobalTransaction  : Begin new global transaction [192.168.10.103:8091:2032177946]
2020-01-08 10:50:00.807  INFO 24032 --- [nio-8104-exec-2] i.s.s.i.c.service.BusinessServiceImpl    : 开始全局事务，XID = 192.168.10.103:8091:2032177946
2020-01-08 10:50:20.235  INFO 24032 --- [nio-8104-exec-2] i.seata.tm.api.DefaultGlobalTransaction  : [192.168.10.103:8091:2032177946] commit status: Committed
```
事务提交成功，

我们来看一下数据库数据变化

t_account 
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190905122211274.png)
t_order

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190905122302472.png)
t_storage

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190905122326182.png)
数据没有问题。

### 5.2 测试回滚
我们`samples-business`将`BusinessServiceImpl`的`handleBusiness2` 下面的代码去掉注释

```
if (!flag) {
  throw new RuntimeException("测试抛异常后，分布式事务回滚！");
}
```
使用postman 发送 ：[http://localhost:8104/business/dubbo/buy2](http://localhost:8104/business/dubbo/buy2) 

.响应结果：

```json
{
    "timestamp": "2019-09-05T04:29:34.178+0000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "测试抛异常后，分布式事务回滚！",
    "path": "/business/dubbo/buy"
}
```
#### 5.2.1 business控制台日志

```
2020-01-08 11:05:21.593  INFO 8168 --- [nio-8104-exec-4] i.s.s.i.c.controller.BusinessController  : 请求参数：BusinessDTO(userId=1, commodityCode=C201901140001, name=fan, count=50, amount=100)
2020-01-08 11:05:25.443  INFO 8168 --- [nio-8104-exec-4] i.s.common.loader.EnhancedServiceLoader  : load ContextCore[null] extension by class[io.seata.core.context.ThreadLocalContextCore]
2020-01-08 11:05:27.148  INFO 8168 --- [nio-8104-exec-4] i.s.common.loader.EnhancedServiceLoader  : load TransactionManager[null] extension by class[io.seata.tm.DefaultTransactionManager]
2020-01-08 11:05:27.148  INFO 8168 --- [nio-8104-exec-4] io.seata.tm.TransactionManagerHolder     : TransactionManager Singleton io.seata.tm.DefaultTransactionManager@64c90f86
2020-01-08 11:05:28.958  INFO 8168 --- [nio-8104-exec-4] i.s.common.loader.EnhancedServiceLoader  : load LoadBalance[null] extension by class[io.seata.discovery.loadbalance.RandomLoadBalance]
2020-01-08 11:05:29.070  INFO 8168 --- [nio-8104-exec-4] i.seata.tm.api.DefaultGlobalTransaction  : Begin new global transaction [192.168.10.103:8091:2032180177]
2020-01-08 11:05:30.310  INFO 8168 --- [nio-8104-exec-4] i.s.s.i.c.service.BusinessServiceImpl    : 开始全局事务，XID = 192.168.10.103:8091:2032180177
2020-01-08 11:05:37.826  INFO 8168 --- [nio-8104-exec-4] i.seata.tm.api.DefaultGlobalTransaction  : [192.168.10.103:8091:2032180177] rollback status: Rollbacked
2020-01-08 11:05:39.492 ERROR 8168 --- [nio-8104-exec-4] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.RuntimeException: 测试抛异常后，分布式事务回滚！] with root cause

java.lang.RuntimeException: 测试抛异常后，分布式事务回滚！
	at io.seata.samples.integration.call.service.BusinessServiceImpl.handleBusiness2(BusinessServiceImpl.java:93) ~[classes/:na]
	at io.seata.samples.integration.call.service.BusinessServiceImpl$$FastClassBySpringCGLIB$$2ab3d645.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218) ~[spring-core-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:769) ~[spring-aop-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163) ~[spring-aop-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747) ~[spring-aop-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at io.seata.spring.annotation.GlobalTransactionalInterceptor$1.execute(GlobalTransactionalInterceptor.java:109) ~[seata-all-1.0.0.jar:1.0.0]
	at io.seata.tm.api.TransactionalTemplate.execute(TransactionalTemplate.java:64) ~[seata-all-1.0.0.jar:1.0.0]
	at io.seata.spring.annotation.GlobalTransactionalInterceptor.handleGlobalTransaction(GlobalTransactionalInterceptor.java:106) ~[seata-all-1.0.0.jar:1.0.0]
	at io.seata.spring.annotation.GlobalTransactionalInterceptor.invoke(GlobalTransactionalInterceptor.java:81) ~[seata-all-1.0.0.jar:1.0.0]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) ~[spring-aop-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747) ~[spring-aop-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:689) ~[spring-aop-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at io.seata.samples.integration.call.service.BusinessServiceImpl$$EnhancerBySpringCGLIB$$3ca9d82e.handleBusiness2(<generated>) ~[classes/:na]
	at io.seata.samples.integration.call.controller.BusinessController.handleBusiness2(BusinessController.java:48) ~[classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_144]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_144]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_144]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_144]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:106) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:888) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:793) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:660) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.2.2.RELEASE.jar:5.2.2.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:526) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:367) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:860) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1591) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_144]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_144]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.29.jar:9.0.29]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_144]
```


#### 5.2.2 account服务控制台日志

```bash
2020-01-08 11:05:35.523  INFO 23416 --- [:20883-thread-8] i.s.s.i.a.dubbo.AccountDubboServiceImpl  : 全局事务id ：192.168.10.103:8091:2032180177
2020-01-08 11:05:35.793  INFO 23416 --- [:20883-thread-8] i.s.common.loader.EnhancedServiceLoader  : load LoadBalance[null] extension by class[io.seata.discovery.loadbalance.RandomLoadBalance]
2020-01-08 11:05:35.908  WARN 23416 --- [:20883-thread-8] i.s.common.loader.EnhancedServiceLoader  : load [io.seata.rm.datasource.undo.parser.ProtostuffUndoLogParser] class fail. io/protostuff/runtime/RuntimeEnv
2020-01-08 11:05:35.909  INFO 23416 --- [:20883-thread-8] i.s.common.loader.EnhancedServiceLoader  : load UndoLogParser[jackson] extension by class[io.seata.rm.datasource.undo.parser.JacksonUndoLogParser]
2020-01-08 11:05:37.281  INFO 23416 --- [atch_RMROLE_1_8] i.s.core.rpc.netty.RmMessageListener     : onMessage:xid=192.168.10.103:8091:2032180177,branchId=2032180189,branchType=AT,resourceId=jdbc:mysql://127.0.0.1:3306/seata,applicationData=null
2020-01-08 11:05:37.283  INFO 23416 --- [atch_RMROLE_1_8] io.seata.rm.AbstractRMHandler            : Branch Rollbacking: 192.168.10.103:8091:2032180177 2032180189 jdbc:mysql://127.0.0.1:3306/seata
2020-01-08 11:05:37.477  INFO 23416 --- [atch_RMROLE_1_8] i.s.r.d.undo.AbstractUndoLogManager      : xid 192.168.10.103:8091:2032180177 branch 2032180189, undo_log deleted with GlobalFinished
2020-01-08 11:05:37.478  INFO 23416 --- [atch_RMROLE_1_8] io.seata.rm.AbstractRMHandler            : Branch Rollbacked result: PhaseTwo_Rollbacked
```
#### 5.2.3 order服务控制台日志

```bash
2020-01-08 11:05:35.492  INFO 17296 --- [:20880-thread-2] i.s.s.i.o.dubbo.OrderDubboServiceImpl    : 全局事务id ：192.168.10.103:8091:2032180177
2020-01-08 11:05:36.470  INFO 17296 --- [:20880-thread-2] i.s.common.loader.EnhancedServiceLoader  : load LoadBalance[null] extension by class[io.seata.discovery.loadbalance.RandomLoadBalance]
2020-01-08 11:05:36.648  WARN 17296 --- [:20880-thread-2] i.s.common.loader.EnhancedServiceLoader  : load [io.seata.rm.datasource.undo.parser.ProtostuffUndoLogParser] class fail. io/protostuff/runtime/RuntimeEnv
2020-01-08 11:05:36.650  INFO 17296 --- [:20880-thread-2] i.s.common.loader.EnhancedServiceLoader  : load UndoLogParser[jackson] extension by class[io.seata.rm.datasource.undo.parser.JacksonUndoLogParser]
2020-01-08 11:05:36.895  INFO 17296 --- [atch_RMROLE_1_8] i.s.core.rpc.netty.RmMessageListener     : onMessage:xid=192.168.10.103:8091:2032180177,branchId=2032180192,branchType=AT,resourceId=jdbc:mysql://127.0.0.1:3306/seata,applicationData=null
2020-01-08 11:05:36.897  INFO 17296 --- [atch_RMROLE_1_8] io.seata.rm.AbstractRMHandler            : Branch Rollbacking: 192.168.10.103:8091:2032180177 2032180192 jdbc:mysql://127.0.0.1:3306/seata
2020-01-08 11:05:37.152  INFO 17296 --- [atch_RMROLE_1_8] i.s.r.d.undo.AbstractUndoLogManager      : xid 192.168.10.103:8091:2032180177 branch 2032180192, undo_log deleted with GlobalFinished
2020-01-08 11:05:37.153  INFO 17296 --- [atch_RMROLE_1_8] io.seata.rm.AbstractRMHandler            : Branch Rollbacked result: PhaseTwo_Rollbacked
```

#### 5.2.4 order服务控制台日志

```bash
2020-01-08 11:05:31.478  INFO 24100 --- [:20888-thread-2] i.s.common.loader.EnhancedServiceLoader  : load ContextCore[null] extension by class[io.seata.core.context.ThreadLocalContextCore]
2020-01-08 11:05:31.478  INFO 24100 --- [:20888-thread-2] i.s.s.i.s.dubbo.StorageDubboServiceImpl  : 全局事务id ：192.168.10.103:8091:2032180177
2020-01-08 11:05:32.097  INFO 24100 --- [:20888-thread-2] i.s.common.loader.EnhancedServiceLoader  : load LoadBalance[null] extension by class[io.seata.discovery.loadbalance.RandomLoadBalance]
2020-01-08 11:05:33.130  WARN 24100 --- [:20888-thread-2] i.s.common.loader.EnhancedServiceLoader  : load [io.seata.rm.datasource.undo.parser.ProtostuffUndoLogParser] class fail. io/protostuff/runtime/RuntimeEnv
2020-01-08 11:05:33.131  INFO 24100 --- [:20888-thread-2] i.s.common.loader.EnhancedServiceLoader  : load UndoLogParser[jackson] extension by class[io.seata.rm.datasource.undo.parser.JacksonUndoLogParser]
2020-01-08 11:05:37.549  INFO 24100 --- [atch_RMROLE_1_8] i.s.core.rpc.netty.RmMessageListener     : onMessage:xid=192.168.10.103:8091:2032180177,branchId=2032180182,branchType=AT,resourceId=jdbc:mysql://127.0.0.1:3306/seata,applicationData=null
2020-01-08 11:05:37.551  INFO 24100 --- [atch_RMROLE_1_8] io.seata.rm.AbstractRMHandler            : Branch Rollbacking: 192.168.10.103:8091:2032180177 2032180182 jdbc:mysql://127.0.0.1:3306/seata
2020-01-08 11:05:37.692  INFO 24100 --- [atch_RMROLE_1_8] i.s.r.d.undo.AbstractUndoLogManager      : xid 192.168.10.103:8091:2032180177 branch 2032180182, undo_log deleted with GlobalFinished
2020-01-08 11:05:37.693  INFO 24100 --- [atch_RMROLE_1_8] io.seata.rm.AbstractRMHandler            : Branch Rollbacked result: PhaseTwo_Rollbacked
```

我们查看数据库数据，已经回滚，和上面的数据一致。

到这里一个简单的案例基本就分析结束。感谢你的学习。

后期会介绍`seata-spring-boot-starter`的实现原理，敬请期待。

最后想一起交流技术的可以加我wx:
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019091810182996.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
