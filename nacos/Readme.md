#微服务架构下如何使用Fescar、Dubbo和Nacos 解决数据一致性

本文将介绍在微服务架构下如何使用Fescar、Dubbo和Nacos来解决业务上的数据一致性问题。   
  

随着业务的快速发展，应用单体架构暴露出代码可维护性差，容错率低，测试难度大，敏捷交付能力差等诸多问题，微服务应运而生。微服务的诞生一方面解决了上述问题，但是另一方面却引入新的问题，其中主要问题之一就是如何解决微服务间的一致性。

[Fescar](https://github.com/alibaba/fescar) 是一款开源的分布式事务解决方案，提供高性能和简单易用的分布式事务服务。

下面将通过一个简单的微服务架构的例子说明如何使用Fescar、Dubbo和Nacos来保证业务数据的一致性。


## 业务案例

用户采购商品业务，整个业务包含3个微服务:

- 库存服务: 扣减给定商品的库存数量。
- 订单服务: 根据采购请求生成订单。
- 账户服务: 用户账户金额扣减。

### 业务结构图

<img src="https://github.com/fescar-group/fescar-samples/blob/master/doc/img/fescar-1.png"  height="200" width="426">


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

### Fescar、Dubbo和Nacos 集成

#### Step 1 初始化MySql数据库（InnoDB 存储引擎）

在 [resources/jdbc.properties](https://github.com/fescar-group/fescar-samples/blob/master/nacos/src/main/resources/jdbc.properties) 修改StorageService、OrderService、AccountService 对应的连接串。

#### Step 2 创建 undo_log（用于Fescar AT 模式）表和相关业务表   


相关建表脚本可在 [resources/sql/](https://github.com/fescar-group/fescar-samples/tree/master/nacos/src/main/resources/sql) 下获取，在相应数据库中执行dubbo_biz.sql中的建表脚本，在每个数据库执行undo_log.sql脚本。

**说明:** 需要保证每个物理库都包含undo_log表，此处可使用一个物理库来表示上述三个微服务对应的逻辑库。

#### Step 3 引入Fescar、Dubbo和Nacos 相关Pom依赖


```xml
           <properties>
               <fescar.version>0.2.0</fescar.version>
               <dubbo.version>2.6.5</dubbo.version>
               <dubbo.registry.nacos.version>0.0.2</dubbo.registry.nacos.version>
            </properties>
        
            <dependency>
                <groupId>com.alibaba.fescar</groupId>
                <artifactId>fescar-spring</artifactId>
                <version>${fescar.version}</version>
            </dependency>
            <dependency>
                <groupId>com.alibaba.fescar</groupId>
                <artifactId>fescar-dubbo</artifactId>
                <version>${fescar.version}</version>
                <exclusions>
                    <exclusion>
                        <artifactId>dubbo</artifactId>
                        <groupId>org.apache.dubbo</groupId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>dubbo</artifactId>
                <version>${dubbo.version}</version>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>dubbo-registry-nacos</artifactId>
                <version>${dubbo.registry.nacos.version}</version>
            </dependency>
```
**说明:** 由于当前apache-dubbo与dubbo-registry-nacos 存在兼容性问题，需要排除fescar-dubbo 中的apache.dubbo依赖手动引入alibaba-dubbo，
后续apache-dubbo(2.7.1+)将兼容dubbo-registry-nacos。


#### Step 4 微服务 Provider Spring配置

分别在三个微服务Spring配置文件（[dubbo-account-service.xml](https://github.com/fescar-group/fescar-samples/blob/master/nacos/src/main/resources/spring/dubbo-account-service.xml)、
[dubbo-order-service](https://github.com/fescar-group/fescar-samples/blob/master/nacos/src/main/resources/spring/dubbo-order-service.xml)和
[dubbo-storage-service.xml](https://github.com/fescar-group/fescar-samples/blob/master/nacos/src/main/resources/spring/dubbo-storage-service.xml)
）配置以下配置：

##### 配置Fescar 代理数据源

```xml
    <bean id="accountDataSourceProxy" class="com.alibaba.fescar.rm.datasource.DataSourceProxy">
        <constructor-arg ref="accountDataSource"/>
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="accountDataSourceProxy"/>
    </bean>
```

此处需要使用com.alibaba.fescar.rm.datasource.DataSourceProxy 包装Druid数据源作为直接业务数据源。DataSourceProxy用于业务sql的拦截解析并与TC交互协调事务状态。

##### 配置Dubbo 注册中心

```xml
    <dubbo:registry address="nacos://${nacos-server-ip}:8848"/>
```

##### 配置Fescar GlobalTransactionScanner

```xml
    <bean class="com.alibaba.fescar.spring.annotation.GlobalTransactionScanner">
        <constructor-arg value="dubbo-demo-account-service"/>
        <constructor-arg value="my_test_tx_group"/>
    </bean>
```
此处构造方法的第一个参数为业务自定义applicationId，若在单机部署多微服务需要保证applicationId 唯一。构造方法的第二个参数
为Fescar事务服务逻辑分组，此分组通过配置中心配置项service.vgroup_mapping.my_test_tx_group 映射到相应的fescar-server 集群名称，然后再根据集群名称.grouplist获取到可用服务列表。

#### Step 5 事务发起方配置

在[dubbo-business.xml](https://github.com/fescar-group/fescar-samples/blob/master/nacos/src/main/resources/spring/dubbo-business.xml)配置以下配置：
##### 配置Dubbo 注册中心

同 Step 4

##### 配置Fescar GlobalTransactionScanner

同 Step 4

##### 在事务发起方service方法上添加@GlobalTransactional注解

```java
@GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
```
timeoutMills为事务的总体超时时间默认60s，name 为事务方法签名的别名，默认为空。注解内参数均可省略。

#### Step 6 启动 Nacos-Server

##### 下载Nacos-Server 最新[release](https://github.com/alibaba/nacos/releases) 包并解压

##### 运行Nacos-server

Linux/Unix/Mac

```bash
sh startup.sh -m standalone
```

Windows

```bash
cmd startup.cmd -m standalone
```

访问Nacos控制台：http://localhost:8848/nacos/index.html#/configurationManagement?dataId=&group=&appName=&namespace=

若访问成功说明Nacos-server服务运行成功（默认账号/密码: nacos/nacos）


#### Step 7 启动 Fescar-Server

##### 下载Fescar-Server 最新[release](https://github.com/alibaba/fescar/releases) 包并解压

##### 初始化Fescar 配置

进入到Fescar-Server 解压目录 conf文件夹下，确认[nacos-config.txt](https://github.com/alibaba/fescar/blob/develop/server/src/main/resources/nacos-config.txt)的配置值（一般不需要修改），确认完成后运行[nacos-config.sh](https://github.com/alibaba/fescar/blob/develop/server/src/main/resources/nacos-config.sh)脚本初始化配置。

```bash
sh nacos-config.sh $Nacos-Server-IP
```
eg:

```bash

sh nacos-config.sh localhost 

```  

脚本执行最后输出 "init nacos config finished, please start fescar-server." 说明推送配置成功，若想进一步确认可登陆Nacos 控制台 配置列表 筛选
Group=FESCAR_GROUP 的配置项

//todo 图片
##### 修改 Fescar-server 服务注册方式为 nacos

进入到Fescar-Server 解压目录 conf文件夹下 [registry.conf](https://github.com/alibaba/fescar/blob/develop/server/src/main/resources/registry.conf) 修改 type="nacos" 并配置Nacos的相关属性。

```properties
registry {
  # file nacos
  type = "nacos"

  nacos {
    serverAddr = "localhost"
    namespace = "public"
    cluster = "default"
  }
  file {
    name = "file.conf"
  }
}

```
type: 可配置为nacos和file，配置为file时无服务注册功能。
nacos.serverAddr: Nacos-Sever 服务地址(不含端口号)
nacos.namespace: Nacos 注册和配置隔离namespace
nacos.cluster: 注册服务的集群名称
file.name: type = "file" classpath下配置文件名


##### 运行Fescar-server

Linux/Unix/Mac

```bash
sh fescar-server.sh $LISTEN_PORT $PATH_FOR_PERSISTENT_DATA $IP(此参数可选)
```

Windows

```bash
cmd fescar-server.bat $LISTEN_PORT $PATH_FOR_PERSISTENT_DATA $IP(此参数可选)

```

$LISTEN_PORT : Fescar-Server 服务端口   
$PATH_FOR_PERSISTENT_DATA : 事务操作记录文件存储路径(已存在路径)
$IP(可选参数): 用于多IP环境下指定Fescar-Server 注册服务的IP   

eg:
sh fescar-server.sh 8091 /home/admin/fescar/data/

运行成功后可在 Nacos 控制台看到 服务名=serverAddr 服务注册列表:

/todo 图片

### Step 7 启动微服务并测试

##### 启动[DubboAccountServiceStarter](https://github.com/fescar-group/fescar-samples/blob/master/nacos/src/main/java/com/alibaba/fescar/samples/nacos/starter/DubboAccountServiceStarter.java)
##### 启动[DubboOrderServiceStarter](https://github.com/fescar-group/fescar-samples/blob/master/nacos/src/main/java/com/alibaba/fescar/samples/nacos/starter/DubboOrderServiceStarter.java)
##### 启动[DubboStorageServiceStarter](https://github.com/fescar-group/fescar-samples/blob/master/nacos/src/main/java/com/alibaba/fescar/samples/nacos/starter/DubboStorageServiceStarter.java)

启动完成可在Nacos 控制台服务列表 看到启动完成的三个provider

//todo

##### 启动[DubboBusinessTester](https://github.com/fescar-group/fescar-samples/blob/master/nacos/src/main/java/com/alibaba/fescar/samples/nacos/starter/DubboBusinessTester.java) 进行测试

**注意:** 在标注@GlobalTransactional 注解方法内部显示的抛出异常才会进行事务的回滚。整个Dubbo 服务调用链路只需要在事务最开始发起方的service方法标注注解即可。


##相关链接:

本文sample地址: https://github.com/fescar-group/fescar-samples/tree/master/nacos   
Fescar: https://github.com/alibaba/fescar   
Dubbo: https://github.com/apache/incubator-dubbo   
Nacos: https://github.com/alibaba/nacos





