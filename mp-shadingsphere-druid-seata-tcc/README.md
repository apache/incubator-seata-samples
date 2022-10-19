# mp-shadingsphere-druid-seata-tcc

## 准备环境

1. 创建mysql 数据库,导入脚本

2. 启动 Nacos server <br>
  [Nacos Server 下载地址](https://github.com/alibaba/nacos/releases)

> 下载最新版本Nacos Server, 本地启动Nacos

注: 分别导入 doc service-A-dev.yml,service-B-dev.yml  配置文件到nacos

3. 启动Seata Server <br>
  [Seata Server 下载地址](https://github.com/seata/seata/releases)

> 下载最新版本Seata Server, 本地启动Seata

4. 启动A 启动B

5. 访问API

    localhost:8070/geneOrder

6. 观察事务回滚效果

> 注释 UserOrderServiceImpl 的代码   int a = 1 / 0; 可以观察事务回滚情况

## 使用组件介绍

* Nacos 注册中心
* Nacos 配置中心
* Open Feign REST 服务调用
* Sentinel 限流熔断
* Seata 分布式事务解决方案

## 项目目录介绍

- mp-shadingsphere-druid-seata-tcc
    - base-common
      `通用常量 vo`
    - base-starter
      `通用star组件`
        - base-core-starter
          `核心依赖，集成了nacos注册配置中心，openfeign,sentinel，validator`
        - base-jdbc-starter
          `持久化依赖，集成了mybatis-plus+druid+shardingsphere(分库分表+读写分离)`
        - base-transaction-starter
          `分布式事务依赖，集成了seata分布式事务(主要使用tcc模式)`
    - demo-service-A
      `订单服务`
    - demo-service-B
      `产品库存服务`
    - doc `sql数据库和yaml配置`

> 注意: 使用seata tcc模式的时候请关闭 enable-auto-data-source-proxy: false 自动代码，否则tcc模式执行后会执行at模式

- 版本

```xml
<mybatis-plus.version>3.4.1</mybatis-plus.version>
<druid.version>1.1.22</druid.version>
<mysql-connector-java.version>8.0.21</mysql-connector-java.version>
<shardingsphere.version>4.0.0-RC2</shardingsphere.version>
```

## demo

场景：<br/>
当创建订单的时候库存-1 <br/>
此时两个服务为分布式事务操作<br/>
> 当两个服务调用完毕的时候1/0，发生异常，两个数据回滚

## 关键代码

```java
@Override
@GlobalTransactional
@Transactional
public void geneOrder(UserOrder userOrder) {
    // 扣减库存
    ResponseEntity<Result> forEntity = restTemplate.getForEntity("http://localhost:8071/deduct?id=1", Result.class);
    if(forEntity.getStatusCode() != HttpStatus.OK ||
            Optional.ofNullable(forEntity.getBody()).orElse(new Result()).getCode() != 200) {
        throw new RuntimeException("扣减库存失败！");
    }
    // 生成订单
    long id = IdWorker.getId();
    userOrder.setId(id);
    userOrderTccAction.geneOrder(userOrder,id);

    int a = 1/0;
}
```

## 运行结果

- 订单服务

```
2021-04-29 15:57:35.909  INFO 9104 --- [nio-8070-exec-1] i.seata.tm.api.DefaultGlobalTransaction  : Begin new global transaction [127.0.0.1:8091:27149561796388640]
2021-04-29 15:57:39.033  INFO 9104 --- [nio-8070-exec-1] ShardingSphere-SQL                       : Rule Type: master-slave
2021-04-29 15:57:39.033  INFO 9104 --- [nio-8070-exec-1] ShardingSphere-SQL                       : SQL: INSERT INTO user_order  ( id,
order_id,
p_id )  VALUES  ( ?,
?,
? ) ::: DataSources: master
2021-04-29 15:57:39.063  INFO 9104 --- [nio-8070-exec-1] c.d.m.o.a.impl.UserOrderTccActionImpl    : geneOrder---------------------1387677438737305602
2021-04-29 15:57:39.149  INFO 9104 --- [h_RMROLE_1_1_12] i.s.c.r.p.c.RmBranchRollbackProcessor    : rm handle branch rollback process:xid=127.0.0.1:8091:27149561796388640,branchId=27149561796388658,branchType=TCC,resourceId=gene-order,applicationData={"actionContext":{"action-start-time":1619683057855,"sys::prepare":"geneOrder","sys::rollback":"cancel","sys::commit":"commit","id":1387677438737305602,"host-name":"169.254.174.68","actionName":"gene-order"}}
2021-04-29 15:57:39.157  INFO 9104 --- [h_RMROLE_1_1_12] io.seata.rm.AbstractRMHandler            : Branch Rollbacking: 127.0.0.1:8091:27149561796388640 27149561796388658 gene-order
2021-04-29 15:57:39.174  INFO 9104 --- [h_RMROLE_1_1_12] c.d.m.o.a.impl.UserOrderTccActionImpl    : cancel---------------------1387677438737305602
2021-04-29 15:57:39.191  INFO 9104 --- [h_RMROLE_1_1_12] ShardingSphere-SQL                       : Rule Type: master-slave
2021-04-29 15:57:39.191  INFO 9104 --- [h_RMROLE_1_1_12] ShardingSphere-SQL                       : SQL: DELETE FROM user_order WHERE id=? ::: DataSources: master
2021-04-29 15:57:39.197  INFO 9104 --- [h_RMROLE_1_1_12] io.seata.rm.AbstractResourceManager      : TCC resource rollback result : true, xid: 127.0.0.1:8091:27149561796388640, branchId: 27149561796388658, resourceId: gene-order
2021-04-29 15:57:39.199  INFO 9104 --- [h_RMROLE_1_1_12] io.seata.rm.AbstractRMHandler            : Branch Rollbacked result: PhaseTwo_Rollbacked
2021-04-29 15:57:39.314  INFO 9104 --- [nio-8070-exec-1] i.seata.tm.api.DefaultGlobalTransaction  : [127.0.0.1:8091:27149561796388640] rollback status: Rollbacked
2021-04-29 15:57:39.338 ERROR 9104 --- [nio-8070-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.ArithmeticException: / by zero] with root cause

java.lang.ArithmeticException: / by zero
```

- 库存服务

```
2021-04-29 15:57:39.221  INFO 4656 --- [h_RMROLE_1_1_12] i.s.c.r.p.c.RmBranchRollbackProcessor    : rm handle branch rollback process:xid=127.0.0.1:8091:27149561796388640,branchId=27149561796388642,branchType=TCC,resourceId=order-decuct,applicationData={"actionContext":{"action-start-time":1619683056387,"sys::prepare":"deduct","sys::rollback":"cancel","sys::commit":"commit","id":1,"host-name":"169.254.174.68","actionName":"order-decuct"}}
2021-04-29 15:57:39.229  INFO 4656 --- [h_RMROLE_1_1_12] io.seata.rm.AbstractRMHandler            : Branch Rollbacking: 127.0.0.1:8091:27149561796388640 27149561796388642 order-decuct
2021-04-29 15:57:39.251  INFO 4656 --- [h_RMROLE_1_1_12] c.d.m.p.a.impl.CompanyProductActionImpl  : cancel---------------------1
2021-04-29 15:57:39.252  INFO 4656 --- [h_RMROLE_1_1_12] ShardingSphere-SQL                       : Rule Type: master-slave
2021-04-29 15:57:39.252  INFO 4656 --- [h_RMROLE_1_1_12] ShardingSphere-SQL                       : SQL: SELECT  id,product_name,account  FROM company_product 
 
 WHERE (id = ?) ::: DataSources: slave02
2021-04-29 15:57:39.257  INFO 4656 --- [h_RMROLE_1_1_12] ShardingSphere-SQL                       : Rule Type: master-slave
2021-04-29 15:57:39.257  INFO 4656 --- [h_RMROLE_1_1_12] ShardingSphere-SQL                       : SQL: UPDATE company_product  SET product_name=?,
account=?  WHERE id=? ::: DataSources: master
2021-04-29 15:57:39.264  INFO 4656 --- [h_RMROLE_1_1_12] io.seata.rm.AbstractResourceManager      : TCC resource rollback result : true, xid: 127.0.0.1:8091:27149561796388640, branchId: 27149561796388642, resourceId: order-decuct
2021-04-29 15:57:39.266  INFO 4656 --- [h_RMROLE_1_1_12] io.seata.rm.AbstractRMHandler            : Branch Rollbacked result: PhaseTwo_Rollbacked
```

感谢seata团队的帮助。