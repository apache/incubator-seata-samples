# 基于 Seata 分布式事务解决案例

## 一、什么是 Seata？

Seata 全称为：Simple Extensible Autonomous Transaction Architecture。该方案基于java实现、简单易用、性能强悍、业务侵入低，是一款能够解决大多数分布式事务场景的极佳选择。原理解析请关注 : https://github.com/seata/seata

## 二、原理浅析和场景展示

该案例实现的微服务架构下的场景示例图：

![SEATA solution](https://camo.githubusercontent.com/b3a71332ae0a91db7f8616286a69b879fcbea672/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f6c61726b2f302f323031382f706e672f31383836322f313534353239363739313037342d33626365376263652d303235652d343563332d393338362d3762393531333564616465382e706e67)


Seata 原理图浅析：

![Typical Process](https://camo.githubusercontent.com/0384806afd7c10544c258ae13717e4229942aa13/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f6c61726b2f302f323031382f706e672f31383836322f313534353239363931373838312d32366661626562392d373166612d346633652d386137612d6663333137643333383966342e706e67)

## 三、核心技术栈

* SpringBoot 2.0.8.RELEASE（2.0以后第一个GA版本）
* Durid 1.1.10（阿里巴巴开源高性能数据源连接池）
* Mybatis 3.4.6（Mybatis-3）
* Dubbo 2.6.5（阿里巴巴开源高性能RPC框架）
* Seata 0.2.1
* Nacos 0.8.0（阿里巴巴开源注册中心/配置中心）

## 四、实现以及规划

* 当前版本实现SpringBoot + Dubbo + Mybatis + Nacos + Seata 技术整合，实现如何在微服务架构中使用分布式事务框架 Seata
* 之后版本将完善流程，并使用Nacos作为配置中心（现在Nacos只是作为注册中心）
* 接下来版本等到 Seata 完善到0.5.0版本后开始支持SringCloud相关技术栈，将实现在Cloud微服务架构中解决分布式事务
* 计划暂定上述

## 五、使用该案例说明

### 1. 准备项目

clone此项目到本地，使用maven构建导入IDEA编辑器中，配置项目使用的maven仓库和JDK版本（1.8）
    
模块说明：
   - samples-account  用户账户微服务模块
   
   - samples-dubbo-business-call  业务发起方模块

   - samples-common  项目公共架构模块
   
   - samples-order  订单微服务模块
   
   - samples-storage  库存微服务模块

### 2. 准备数据库

创建数据库（默认为：db_gts_fescar），将sql目录中的sql脚本导入到数据库

执行完毕后，数据库中有如下表：

```
+-------------------------+
| Tables_in_db_gts_fescar |
+-------------------------+
| t_account               |
| t_order                 |
| t_storage               |
| undo_log                |
+-------------------------+
```

### 3. 启动 Nacos

Nacos 快速启动参考：https://nacos.io/en-us/docs/quick-start.html

进入 Nacos 控制台表示启动成功：http://127.0.0.1:8848/nacos/index.html
   
### 4. 启动 Seata Server
  
下载页面：https://github.com/seata/seata/releases

下载并解压 seata-server，进入bin目录，执行启动命令

```bash
sh seata-server.sh 8091 file
```

### 5. 启动各模块服务

分别启动 samples-account、samples-order、samples-storage、samples-dubbo-business-call 四个模块，确定微服务都注册到 Nacos 和 Seata

进入 Nacos 控制台确认服务注册情况: http://127.0.0.1:8848/nacos/#/serviceManagement

> 注意检查各个模块下 application.properties 中的 datasource 配置与本地数据库一致
    
### 6. 模拟请求

使用Postman工具请求Post接口地址：http://localhost:8104/business/dubbo/buy  
参数示例：

```json
{
    "userId":"1",
    "commodityCode":"P190510529590122",
    "name":"风扇",
    "count":2,
    "amount":"100"
}
```

或使用curl命令：

```bash
curl -H "Content-Type:application/json" -X POST -d '{"userId":"1","commodityCode":"P190510529590122","name":"风扇","count":2,"amount":"100"}' localhost:8104/business/dubbo/buy
``` 

模拟发起下单业务请求，成功后返回200

### 7. 模拟回滚

进入samples-dubbo-business-call模块下的 BusinessServiceImpl类，打开被注释的代码：

```
if (!flag) {
  throw new RuntimeException("测试抛异常后，分布式事务回滚！");
}
```

重新启动 samples-dubbo-business-call 模块，再次发起请求，发生异常，全局事物被回滚
