面我已经写过一篇[SpringBoot+Nacos+Seata实现Dubbo分布式事务管理](https://blog.csdn.net/u010046908/article/details/100536439)的文章，今天为什么还要写这篇呢，是因为好多公司还在用`Zookeeper`作为`Dubbo`的注册中心和配置中心在大规模使用，还没有完全迁移到`Nacos`上来，所以`Seata`的注册中心和配置也是支持`Zookeeper`，但是官方没有完整的使用教程，因此，写这篇主要为了帮助使用`Zookeeper`的用户也可以轻松使用`Seata`。

## 1.简介

> 本文主要介绍SpringBoot2.1.5 + Dubbo 2.7.3 + Mybatis 3.4.2 + Zookeeper 3.4.14 +Seata 0.9.0整合来实现Dubbo分布式事务管理，使用Zookeeper 作为 Dubbo和Seata的注册中心和配置中心,使用 MySQL 数据库和 MyBatis来操作数据。

如果你还对`SpringBoot`、`Dubbo`、`Zookeeper`、`Seata`、` Mybatis` 不是很了解的话，这里我为大家整理个它们的官网网站，如下

- SpringBoot：[https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)

- Dubbo：[http://dubbo.apache.org/en-us/](http://dubbo.apache.org/en-us/)

- Zookeeper：[https://zookeeper.apache.org/](https://zookeeper.apache.org/)

- Seata：[https://github.com/seata/seata/wiki/Home_Chinese](https://github.com/seata/seata/wiki/Home_Chinese)

- MyBatis：[http://www.mybatis.org/mybatis-3/zh/index.html](http://www.mybatis.org/mybatis-3/zh/index.html)

在这里我们就不一个一个介绍它们是怎么使用和原理，详细请学习官方文档，在这里我将开始对它们进行整合，完成一个简单的案例，来让大家了解`Seata`来实现`Dubbo`分布式事务管理的基本流程。

## 2.环境准备

## 2.1 下载Zookeeper并安装启动

Zookeeper下载：[https://archive.apache.org/dist/zookeeper/zookeeper-3.4.14/](https://archive.apache.org/dist/zookeeper/zookeeper-3.4.14/)

Zookeeper
快速入门：[http://zookeeper.apache.org/doc/r3.4.14/zookeeperStarted.html](http://zookeeper.apache.org/doc/r3.4.14/zookeeperStarted.html)

在单机模式下启动`Zookeeper`非常简单。
我们将下载的文件解压缩到指定目录如：`E:\tools\zookeeper-3.4.14`![在这里插入图片描述](https://img-blog.csdnimg.cn/20191021103212905.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
要启动`Zookeeper`，我们需要一个配置文件。下面是一个示例，在conf/zoo.cfg:

```
tickTime=2000
initLimit=10
syncLimit=5
clientPort=2181
dataDir=E:\\tools\\zookeeper-3.4.14\\data
dataLogDir=E:\\tools\\zookeeper-3.4.14\\logs
```

以上配置的详细说明如下：

- `tickTime`：用于配置 ZooKeeper 中最小时间单位的长度，很多运行时的时间间隔都是使用 tickTime 的倍数来表示的。例如，ZooKeeper 中会话的最小超时时间默认是 2*tickTime。
- `initLimit`：该参数有默认值: 10，即表示是参数 tickTime 值得10倍，必须配置，且需要配置一个正整数，不支持系统属性方式配置。该参数用于配置 Leader 服务器等待 Follower
  启动，并完成数据同步的时间。Follwer 服务器再启动过程中，会与 Leader 建立连接并完成对数据的同步，从而确定自己对外提供服务的其实状态。Leader 服务器允许 Follower 在 initLimit 时间内完成这个工作。
  通常情况下，运维人员不用太在意这个参数的配置，使用默认值即可。但如果随着 ZooKeeper 集群管理的数据量增大，Follower 服务器再启动的时候，从 Leader
  上进行同步数据的时间也会相应变长，于是无法在较短的时间完成数据同步。因此，在这种情况下，有必要适当调大这个参数。
- `syncLimit`:该参数有默认值: 5，即表示参数 tickTime 值得5倍，必须配置，且需要配置一个正整数，不支持系统属性配置。该参数用于配置 Leader 服务器和 Follower 服务器之间进行心跳检测的最大延时时间。在
  ZooKeeper 集群运行过程中，Leader 服务器会与所有的 Follower 进行心跳检测来确定该服务器是否存活。如果 Leader 服务器再 syncLimit 时间内无法获取到 Follower 的心跳检测响应，那么
  Leader 就会认为该 Follower 已经脱离了和自己的同步。通常情况下，运维人员使用该参数的默认值即可，但如果部署ZooKeeper 集群的网络环境质量较低（例如网络延时较大或丢包严重），那么可以适当调大这个参数。
- `dataDir`: 该参数有默认值: dataDir，可以不配置，不支持系统属性方式配置。参数 dataLogDir 用于配置 ZooKeeper 服务器存储事务日志文件的目录。默认情况下，ZooKeeper
  会将事务日志文件和快照数据存储在同一目录中，应该尽量将这两者的牧区区分开来。另外，如果条件允许，可以将事务日志的存储位置配置在一个单独的磁盘上。事务日志记录对于磁盘的性能要求非常高，为了保证数据的一致性，ZooKeeper
  在返回客户端事务请求相应之前，必须将本次请求对应的事务日志写入到磁盘中。因此，事务日志写入的性能直接决定了 ZooKeeper 在处理事务请求时的吞吐。针对同一块磁盘的其他并发读写操作（例如 ZooKeeper
  运行时日志输出和操作系统自身的读写等），尤其是数据快照操作，会极大地影响事务日志的写性能。因此尽量给事务日志的输出配置一个单独的磁盘或挂载点，将极大地提升 ZooKeeper 的整体性能。

- `clientPort` :用于配置当前服务器对外的服务端口，客户端会通过该端口和 ZooKeeper 服务器创建连接，一般设置为2181。每台 ZooKeeper 都可以配置任意可用的端口，同时集群中的所有服务器不需要保持
  clientPort 端口一致。该参数无默认值，必须配置。

`dataLogDir`:存储日志的目录

启动Zookeeper：

```shell
bin/zkServer.cmd start
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191021104446797.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
这是时候`ZooKeeper` 服务就正常启动了。

我们可以使客户端去来连接

```shell
bin/zkCli.cmd 
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191021104720936.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)

## ZooKeeper -server host:port cmd args

```sql
   stat path [watch]
        set path data [version]
        ls path [watch]
        delquota [-n|-b] path
        ls2 path [watch]
        setAcl path acl
        setquota -n|-b val path
        history
        redo cmdno
        printwatches on|off
        delete path [version]
        sync path
        listquota path
        rmr path
        get path [watch]
        create [-s] [-e] path data acl
        addauth scheme auth
        quit
        getAcl path
        close
        connect host:port
```

我们是用`ls`查询节点情况

```bash
ls /
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20191021104916700.png)
默认有一个`zookeeoer`节点。

## 2.2 下载seata0.9.0 并安装启动

#### 2.2.1 在 [Seata Release](https://github.com/seata/seata/releases/tag/v0.9.0) 下载最新版的 Seata Server 并解压得到如下目录：

```shell
.
├──bin
├──conf
└──lib
```

#### 2.2.2 修改 conf/registry.conf 和file.conf配置，

目前seata支持如下的file、nacos 、apollo、zk、consul的注册中心和配置中心。这里我们以`zk` 为例。 将 type 改为 zk

```shell
registry {
  # file zk
  type = "zk"

 zk {
    cluster = "default"
    serverAddr = "127.0.0.1:2181"
    session.timeout = 6000
    connect.timeout = 2000
  }
  file {
    name = "file.conf"
  }
}

config {
  # file、nacos 、apollo、zk、consul
  type = "zk"

  zk {
    serverAddr = "127.0.0.1:2181"
    session.timeout = 6000
    connect.timeout = 2000
  }

  file {
    name = "file.conf"
  }
}

```

- serverAddr = "127.0.0.1:2181"   ：zk 的地址
- cluster = "default"  ：集群设置为默认 `default`
- session.timeout = 6000 ：会话的超时时间
- connect.timeout = 2000：连接的超时时间

file.conf 配置

```bash
transport {
  # tcp udt unix-domain-socket
  type = "TCP"
  #NIO NATIVE
  server = "NIO"
  #enable heartbeat
  heartbeat = true
  #thread factory for netty
  thread-factory {
    boss-thread-prefix = "NettyBoss"
    worker-thread-prefix = "NettyServerNIOWorker"
    server-executor-thread-prefix = "NettyServerBizHandler"
    share-boss-worker = false
    client-selector-thread-prefix = "NettyClientSelector"
    client-selector-thread-size = 1
    client-worker-thread-prefix = "NettyClientWorkerThread"
    # netty boss thread size,will not be used for UDT
    boss-thread-size = 1
    #auto default pin or 8
    worker-thread-size = 8
  }
  shutdown {
    # when destroy server, wait seconds
    wait = 3
  }
  serialization = "seata"
  compressor = "none"
}
service {
  #vgroup->rgroup
  vgroup_mapping.my_test_tx_group = "default"
  #only support single node
  default.grouplist = "127.0.0.1:8091"
  #degrade current not support
  enableDegrade = false
  #disable
  disable = false
  #unit ms,s,m,h,d represents milliseconds, seconds, minutes, hours, days, default permanent
  max.commit.retry.timeout = "-1"
  max.rollback.retry.timeout = "-1"
}

client {
  async.commit.buffer.limit = 10000
  lock {
    retry.internal = 10
    retry.times = 30
  }
  report.retry.count = 5
  tm.commit.retry.count = 1
  tm.rollback.retry.count = 1
}

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
    driver-class-name = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://127.0.0.1:3306/seata"
    user = "root"
    password = "123456"
    min-conn = 1
    max-conn = 3
    global.table = "global_table"
    branch.table = "branch_table"
    lock-table = "lock_table"
    query-limit = 100
  }
}
lock {
  ## the lock store mode: local、remote
  mode = "remote"

  local {
    ## store locks in user's database
  }

  remote {
    ## store locks in the seata's server
  }
}
recovery {
  #schedule committing retry period in milliseconds
  committing-retry-period = 1000
  #schedule asyn committing retry period in milliseconds
  asyn-committing-retry-period = 1000
  #schedule rollbacking retry period in milliseconds
  rollbacking-retry-period = 1000
  #schedule timeout retry period in milliseconds
  timeout-retry-period = 1000
}

transaction {
  undo.data.validation = true
  undo.log.serialization = "jackson"
  undo.log.save.days = 7
  #schedule delete expired undo_log in milliseconds
  undo.log.delete.period = 86400000
  undo.log.table = "undo_log"
}

## metrics settings
metrics {
  enabled = false
  registry-type = "compact"
  # multi exporters use comma divided
  exporter-list = "prometheus"
  exporter-prometheus-port = 9898
}

support {
  ## spring
  spring {
    # auto proxy the DataSource bean
    datasource.autoproxy = false
  }
}
```

主要修改了`store.mode`为`db`,还有数据库相关的配置

#### 2.2.3 修改 conf/nacos-config.txt配置为zk-config.properties

```shell
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
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true
store.db.user=root
store.db.password=123456
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
```

这里主要修改了如下几项：

- store.mode :存储模式 默认file 这里我修改为db 模式 ，并且需要三个表`global_table`、`branch_table`和`lock_table`
- store.db.driver-class-name： 默认没有，会报错。添加了 `com.mysql.jdbc.Driver`
- store.db.datasource=dbcp ：数据源 dbcp
- store.db.db-type=mysql : 存储数据库的类型为`mysql`
- store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true : 修改为自己的数据库`url`、`port`、`数据库名称`
- store.db.user=root :数据库的账号
- store.db.password=123456 :数据库的密码
- service.vgroup_mapping.order-service-seata-service-group=default
- service.vgroup_mapping.account-service-seata-service-group=default
- service.vgroup_mapping.storage-service-seata-service-group=default
- service.vgroup_mapping.business-service-seata-service-group=default

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

#### 2.2.4 将 Seata 配置添加到 Zookeeper 中

用于官方只提供了nacos的脚本配置。我这用java实现了将 Seata 配置添加到 Zookeeper 中。 我这里参考了`ZookeeperConfiguration`的源码实现的导入到zk的初始化代码。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191021121251985.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
通过查看以上源代码我们可以看出 zk模式下数据的存储格式。并且使用的zk的永久节点存储。

###### 2.4.2.1.创建根节点，其中根节点为`/config`

```java
public ZookeeperConfiguration() {
        if (zkClient == null) {
            Class var1 = ZookeeperConfiguration.class;
            synchronized(ZookeeperConfiguration.class) {
                if (null == zkClient) {
                    zkClient = new ZkClient(FILE_CONFIG.getConfig("config.zk.serverAddr"), FILE_CONFIG.getInt("config.zk.session.timeout", 6000), FILE_CONFIG.getInt("config.zk.connect.timeout", 2000));
                }
            }

            if (!zkClient.exists("/config")) {
                zkClient.createPersistent("/config", true);
            }
        }

    }
```

###### 2.4.2.2添加配置的方法

```java
public boolean putConfig(final String dataId, final String content, long timeoutMills) {
        FutureTask<Boolean> future = new FutureTask(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                String path = "/config/" + dataId;
                if (!ZookeeperConfiguration.zkClient.exists(path)) {
                    ZookeeperConfiguration.zkClient.create(path, content, CreateMode.PERSISTENT);
                } else {
                    ZookeeperConfiguration.zkClient.writeData(path, content);
                }

                return true;
            }
        });
        CONFIG_EXECUTOR.execute(future);

        try {
            return (Boolean)future.get(timeoutMills, TimeUnit.MILLISECONDS);
        } catch (Exception var7) {
            LOGGER.warn("putConfig {} : {} is error or timeout", dataId, content);
            return false;
        }
    }
```

###### 2.4.2.3 使用java读取zk-config.properties配置文件

```java
package io.seata.samples.integration.call;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * 
 */
@Slf4j
public class ZkDataInit {

    private static volatile ZkClient zkClient;

    public static void main(String[] args) {
        if (zkClient == null) {
            zkClient = new ZkClient("127.0.0.1:2181", 6000, 2000);
        }
        if (!zkClient.exists("/config")) {
            zkClient.createPersistent("/config", true);
        }
        //获取key对应的value值
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        // 使用properties对象加载输入流
        try {
            File file = ResourceUtils.getFile("classpath:zk-config.properties");
            InputStream in = new FileInputStream(file);
            properties.load(in);
            Set<Object> keys = properties.keySet();//返回属性key的集合
            for (Object key : keys) {
                boolean b = putConfig(key.toString(), properties.get(key).toString());
                log.info(key.toString() + "=" + properties.get(key)+"result="+b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param dataId
     * @param content
     * @return
     */
    public static boolean putConfig(final String dataId, final String content) {
        Boolean flag = false;
        String path = "/config/" + dataId;
        if (!zkClient.exists(path)) {
            zkClient.create(path, content, CreateMode.PERSISTENT);
            flag = true;
        } else {
            zkClient.writeData(path, content);
            flag = true;
        }
        return flag;
    }
}

```

###### 2.4.2.4 查看zk的节点结构

```shell
[zk: localhost:2181(CONNECTED) 3] ls /config
[metrics.exporter-prometheus-port, store.file.session.reload.read_size, recovery
.committing-retry-period, store.db.lock-table, store.db.datasource, transport.th
read-factory.client-selector-thread-prefix, transaction.undo.log.save.days, metr
ics.exporter-list, transport.server, client.async.commit.buffer.limit, store.fil
e.max-branch-session-size, transport.thread-factory.client-selector-thread-size,
 transaction.undo.log.delete.period, transaction.undo.data.validation, service.d
isableGlobalTransaction, transport.thread-factory.boss-thread-size, client.lock.
retry.times, service.max.commit.retry.timeout, store.db.driver-class-name, store
.file.flush-disk-mode, transport.thread-factory.worker-thread-size, store.mode,
transport.serialization, transport.thread-factory.client-worker-thread-prefix, s
tore.file.dir, recovery.rollbacking-retry-period, store.db.query-limit, transpor
t.compressor, store.db.url, store.db.user, recovery.timeout-retry-period, servic
e.disable, store.db.db-type, client.report.retry.count, store.file.file-write-bu
ffer-cache-size, transaction.undo.log.table, client.lock.retry.internal, transac
tion.undo.log.serialization, recovery.asyn-committing-retry-period, metrics.enab
led, store.db.password, transport.thread-factory.worker-thread-prefix, transport
.thread-factory.boss-thread-prefix, service.vgroup_mapping.storage-service-seata
-service-group, service.vgroup_mapping.order-service-seata-service-group, store.
db.global.table, store.db.branch.table, service.vgroup_mapping.account-service-s
eata-service-group, service.vgroup_mapping.business-service-seata-service-group,
 service.max.rollback.retry.timeout, service.enableDegrade, store.file.max-globa
l-session-size, transport.type, store.db.max-conn, transport.thread-factory.shar
e-boss-worker, transport.thread-factory.server-executor-thread-prefix, metrics.r
egistry-type, transport.heartbeat, transport.shutdown.wait, store.db.min-conn]
```

所有的配置都已经导入成功。

#### 2.2.5 启动 Seata Server

使用db 模式启动

```shell
 cd ..
bin/seata-server.bat -m db
```

这时候在 zookeeper 的我们在客户端可以使使用命令查询节点

```shell
[zk: localhost:2181(CONNECTED) 4] ls /
registry    zookeeper   config
[zk: localhost:2181(CONNECTED) 4] ls /registry/zk/default
[192.168.10.108:8091]
```

看到这，seata server就已经搭建成功

## 3 案例分析

参考官网中用户购买商品的业务逻辑。整个业务逻辑由4个微服务提供支持：

- 库存服务：扣除给定商品的存储数量。
- 订单服务：根据购买请求创建订单。
- 帐户服务：借记用户帐户的余额。
- 业务服务：处理业务逻辑。

请求逻辑架构
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190905111031350.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)

## 3.1  github地址

springboot-dubbo-seata：[https://github.com/lidong1665/springboot-dubbo-seata-zk](https://github.com/lidong1665/springboot-dubbo-seata-zk)

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

#### 3.4 库存服务：StorageDubboService

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

创建数据库 并导入数据库脚本

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
        <version>2.1.5.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <artifactId>springboot-dubbo-seata</artifactId>
    <packaging>pom</packaging>
    <name>springboot-dubbo-seata</name>
    <groupId>io.seata</groupId>
    <version>1.0.0</version>
    <description>Demo project for Spring Cloud Alibaba Dubbo</description>

    <modules>
        <module>samples-common</module>
        <module>samples-account</module>
        <module>samples-order</module>
        <module>samples-storage</module>
        <module>samples-business</module>
    </modules>

    <properties>
        <springboot.verison>2.1.5.RELEASE</springboot.verison>
        <java.version>1.8</java.version>
        <druid.version>1.1.10</druid.version>
        <mybatis.version>1.3.2</mybatis.version>
        <mybatis-plus.version>2.3</mybatis-plus.version>
        <lombok.version>1.16.22</lombok.version>
        <dubbo.version>2.7.3</dubbo.version>
        <seata.version>0.9.0</seata.version>
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
            <artifactId>dubbo-registry-zookeeper</artifactId>
            <version>${dubbo.version}</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.seata/seata-all -->
        <dependency>
            <groupId>io.seata</groupId>
            <artifactId>seata-all</artifactId>
            <version>${seata.version}</version>
        </dependency>


        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-dependencies-zookeeper</artifactId>
            <version>${dubbo.version}</version>
            <type>pom</type>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.14</version>
        </dependency>

        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>4.0.1</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.101tec/zkclient -->
        <dependency>
            <groupId>com.101tec</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.11</version>
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
            <version>1.0.2</version>
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

- `seata-all`: 这个是seata 所需的主要依赖。
- `dubbo-spring-boot-starter`:   springboot dubbo的依赖。

其他的就不一一介绍，其他的一目了然，就知道是干什么的。

### 3.8.2  application.properties配置

```properties
server.port=8102
spring.application.name=dubbo-account-example

#====================================Dubbo config===============================================
dubbo.application.id= dubbo-account-example
dubbo.application.name= dubbo-account-example
dubbo.protocol.id=dubbo
dubbo.protocol.name=dubbo
dubbo.registry.id=dubbo-account-example-registry
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.protocol.port=20880
dubbo.application.qos-enable=false
dubbo.config-center.address=zookeeper://127.0.0.1:2181
dubbo.metadata-report.address=zookeeper://127.0.0.1:2181

#====================================mysql config============================================
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/seata?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.username=root
spring.datasource.password=123456


#=====================================mybatis config======================================
mybatis.mapper-locations=classpath*:/mapper/*.xml
```

### 3.8.3 registry.conf（zk的配置）

##### registry.conf的配置

```conf
registry {
  # file 、nacos 、eureka、redis、zk
  type = "zk"

  nacos {
    serverAddr = "localhost"
    namespace = "public"
    cluster = "default"
  }
  eureka {
    serviceUrl = "http://localhost:1001/eureka"
    application = "default"
    weight = "1"
  }
  redis {
    serverAddr = "127.0.0.1:6379"
    db = "0"
  }
  zk {
    cluster = "default"
    serverAddr = "localhost:2181"
    session.timeout = 6000
    connect.timeout = 2000
  }
  file {
    name = "file.conf"
  }
}

config {
  # file、nacos 、apollo、zk
  type = "zk"

  nacos {
    serverAddr = "localhost"
    namespace = "public"
    cluster = "default"
  }
  apollo {
    app.id = "fescar-server"
    apollo.meta = "http://192.168.1.204:8801"
  }
  zk {
    serverAddr = "127.0.0.1:2181"
    session.timeout = 6000
    connect.timeout = 2000
  }
  file {
    name = "file.conf"
  }
}

```

### 3.8.5 SeataAutoConfig 配置

```java
package io.seata.samples.integration.account.config;

import com.alibaba.druid.pool.DruidDataSource;

import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @Author: llidong
 * @Description  seata global configuration
 * @Date Created in 2019/9/05 10:28
 */
@Configuration
public class SeataAutoConfig {

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
     * init datasource proxy
     * @Param: druidDataSource  datasource bean instance
     * @Return: DataSourceProxy  datasource proxy
     */
    @Bean
    public DataSourceProxy dataSourceProxy(DruidDataSource druidDataSource){
        return new DataSourceProxy(druidDataSource);
    }

    /**
     * init mybatis sqlSessionFactory
     * @Param: dataSourceProxy  datasource proxy
     * @Return: DataSourceProxy  datasource proxy
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceProxy);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mapper/*.xml"));
        return factoryBean.getObject();
    }

    /**
     * init global transaction scanner
     *
     * @Return: GlobalTransactionScanner
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner(){
        return new GlobalTransactionScanner("account-gts-seata-example", "account-service-seata-service-group");
    }
}

```

其中:

```
@Bean
    public GlobalTransactionScanner globalTransactionScanner(){
        return new GlobalTransactionScanner("account-gts-seata-example", "account-service-seata-service-group");
    }
```

`GlobalTransactionScanner`: 初始化全局的事务扫描器

```
 /**
     * Instantiates a new Global transaction scanner.
     *
     * @param applicationId  the application id
     * @param txServiceGroup the default server group
     */
    public GlobalTransactionScanner(String applicationId, String txServiceGroup) {
        this(applicationId, txServiceGroup, DEFAULT_MODE);
    }
```

- applicationId ：为应用id 这里我传入的是`account-gts-seata-example`
- txServiceGroup: 默认server的分组 这里我传入的是`account-service-seata-service-group` 这个和我们前面在nacos 配置的是保存一致。
- DEFAULT_MODE：默认的事务模式 为[AT_MODE](https://github.com/seata/seata/wiki/AT-Mode)
    + [MT_MODE](https://github.com/seata/seata/wiki/MT-Mode)

### 3.8.6 AccountExampleApplication 启动类的配置

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

并且在zk的客户端查看注册情况

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
2019-10-21 12:04:54.816  INFO 12780 --- [nio-8104-exec-1] i.s.s.i.c.controller.BusinessController  : 请求参数：BusinessDTO(userId=1, commodityCode=C201901140001, name=fan, count=50, amount=100)
2019-10-21 12:04:54.823  INFO 12780 --- [nio-8104-exec-1] i.s.common.loader.EnhancedServiceLoader  : load ContextCore[null] extension by class[io.seata.core.context.ThreadLocalContextCore]
2019-10-21 12:04:54.829 ERROR 12780 --- [nio-8104-exec-1] i.s.config.zk.ZookeeperConfiguration     : getConfig client.tm.commit.retry.count is error or timeout,return defaultValue 1
2019-10-21 12:04:54.830 ERROR 12780 --- [nio-8104-exec-1] i.s.config.zk.ZookeeperConfiguration     : getConfig client.tm.rollback.retry.count is error or timeout,return defaultValue 1
2019-10-21 12:04:54.833  INFO 12780 --- [nio-8104-exec-1] i.s.common.loader.EnhancedServiceLoader  : load TransactionManager[null] extension by class[io.seata.tm.DefaultTransactionManager]
2019-10-21 12:04:54.833  INFO 12780 --- [nio-8104-exec-1] io.seata.tm.TransactionManagerHolder     : TransactionManager Singleton io.seata.tm.DefaultTransactionManager@394cfa5a
2019-10-21 12:04:54.840  INFO 12780 --- [nio-8104-exec-1] i.s.common.loader.EnhancedServiceLoader  : load LoadBalance[null] extension by class[io.seata.discovery.loadbalance.RandomLoadBalance]
2019-10-21 12:04:55.037  INFO 12780 --- [nio-8104-exec-1] i.seata.tm.api.DefaultGlobalTransaction  : Begin new global transaction [192.168.10.108:8091:2025358030]
2019-10-21 12:04:57.473  INFO 12780 --- [nio-8104-exec-1] i.s.s.i.c.service.BusinessServiceImpl    : 开始全局事务，XID = 192.168.10.108:8091:2025358030
2019-10-21 12:05:04.280  INFO 12780 --- [nio-8104-exec-1] i.seata.tm.api.DefaultGlobalTransaction  : [192.168.10.108:8091:2025358030] commit status:Committed
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

控制台

```shell
2019-10-21 12:05:46.752  INFO 12780 --- [nio-8104-exec-3] i.s.s.i.c.controller.BusinessController  : 请求参数：BusinessDTO(userId=1, commodityCode=C201901140001, name=fan, count=50, amount=100)
2019-10-21 12:05:46.785  INFO 12780 --- [nio-8104-exec-3] i.seata.tm.api.DefaultGlobalTransaction  : Begin new global transaction [192.168.10.108:8091:2025358056]
2019-10-21 12:05:46.786  INFO 12780 --- [nio-8104-exec-3] i.s.s.i.c.service.BusinessServiceImpl    : 开始全局事务，XID = 192.168.10.108:8091:2025358056
2019-10-21 12:05:50.285  INFO 12780 --- [nio-8104-exec-3] i.seata.tm.api.DefaultGlobalTransaction  : [192.168.10.108:8091:2025358056] rollback status:Rollbacked
2019-10-21 12:05:50.477 ERROR 12780 --- [nio-8104-exec-3] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.RuntimeException: 测试抛异常后，分布式事务回滚！] with root cause

java.lang.RuntimeException: 测试抛异常后，分布式事务回滚！
	at io.seata.samples.integration.call.service.BusinessServiceImpl.handleBusiness2(BusinessServiceImpl.java:93) ~[classes/:na]
	at io.seata.samples.integration.call.service.BusinessServiceImpl$$FastClassBySpringCGLIB$$2ab3d645.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218) ~[spring-core-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:749) ~[spring-aop-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163) ~[spring-aop-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at io.seata.spring.annotation.GlobalTransactionalInterceptor$1.execute(GlobalTransactionalInterceptor.java:104) ~[seata-all-0.9.0.jar:0.9.0]
	at io.seata.tm.api.TransactionalTemplate.execute(TransactionalTemplate.java:64) ~[seata-all-0.9.0.jar:0.9.0]
	at io.seata.spring.annotation.GlobalTransactionalInterceptor.handleGlobalTransaction(GlobalTransactionalInterceptor.java:101) ~[seata-all-0.9.0.jar:0.9.0]
	at io.seata.spring.annotation.GlobalTransactionalInterceptor.invoke(GlobalTransactionalInterceptor.java:76) ~[seata-all-0.9.0.jar:0.9.0]
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186) ~[spring-aop-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688) ~[spring-aop-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at io.seata.samples.integration.call.service.BusinessServiceImpl$$EnhancerBySpringCGLIB$$a8aa15d5.handleBusiness2(<generated>) ~[classes/:na]
	at io.seata.samples.integration.call.controller.BusinessController.handleBusiness2(BusinessController.java:48) ~[classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_144]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_144]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_144]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_144]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:104) ~[spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:892) ~[spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797) ~[spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1039) ~[spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942) ~[spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005) ~[spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:908) ~[spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:660) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:882) ~[spring-webmvc-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:741) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:92) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:93) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:200) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-5.1.7.RELEASE.jar:5.1.7.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:200) ~[tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:490) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:408) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:836) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1747) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_144]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_144]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.19.jar:9.0.19]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_144]

```

我们查看数据库数据，已经回滚，和上面的数据一致。

到这里一个简单的`SpringBoot+Zookeeper+Seata实现Dubbo分布式事务管理`案例基本就分析结束。感谢你的学习。

最后想一起交流技术的可以加我wx:
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019091810182996.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9saWRvbmcxNjY1LmJsb2cuY3Nkbi5uZXQ=,size_16,color_FFFFFF,t_70)
