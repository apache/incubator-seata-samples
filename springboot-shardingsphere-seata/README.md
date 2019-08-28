# SpringBoot 使用 Seata+Shardingsphere+Dubbo+Nacos+MybatisPlus+DruidDataSource

> 使用 Seata 作为分布式事务组件，使用 MySQL 数据库，使用 MyBatisPlus 作为数据访问层实现多数据源下事务一致

## 环境准备

### 创建数据库及表

执行 `sql目录下的sql文件,必须先创建好demo_ds_0、demo_ds_1数据库，分别执行sql文件,ShardingSphere分库分表组件规则即为必须先创建好数据库和分表`

### 启动 Seata Server 

可以直接通过bash 脚本启动 Seata Server，也可以通过 Docker 镜像启动，但是 Docker 方式目前只支持使用 file 模式，不支持将 Seata-Server 注册到 Eureka 或 Nacos 等注册中心
也可以直接运行seata-server.bat

- 通过seata-server.bat脚本启动

在 [Seata Release](https://github.com/seata/seata/releases) 下载相应版本的 Seata Server，解压后执行以下命令启动，这里使用 file 配置
例子使用的seata-server版本为0.7.1版本,下载0.7.1版本进行测试,因为例子使用file文件模式,所以需要修改seata-server-0.7.1/conf/file.conf文件
修改内容大致如下：
```bash
service {
  #vgroup->rgroup
  vgroup_mapping.my_test_tx_group = "default"
  #此处是重点,raw-jdbc-group和resources目录下seata.conf文件中transaction.service.group需要匹配,若seata-server服务端未配置该项,会出现TM/RM无法连接seata-server日志输出
  vgroup_mapping.raw-jdbc-group = "default"
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
```

```bash
seata-server.bat运行
```

## 测试

- 启动
- Nacos->Seata-server->ShardingsphereSeataOederApplication->ShardingsphereSeataBusinessApplication

- 测试成功场景

调用 Business 接口

```bash
curl -X GET \
  http://localhost:8094/test
```

最后查询数据库,数据已经回滚

