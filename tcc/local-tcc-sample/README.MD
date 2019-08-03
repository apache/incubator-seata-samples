# Seata TCC 模式 demo 工程


[Seata](https://github.com/seata/seata) 是一款开源的分布式事务解决方案，提供高性能和简单易用的分布式事务服务。   


## demo 说明

基于 Seata TCC模式，演示分布式事务的提交和回滚；

本demo中一个分布式事务内会有2个TCC事务参与者，分别是: [TccActionOne](https://github.com/seata/seata-samples/blob/master/tcc/local-tcc-sample/src/main/java/io/seata/samples/tcc/action/TccActionOne.java) 和 [TccActionTwo](https://github.com/seata/seata-samples/blob/master/tcc/local-tcc-sample/src/main/java/io/seata/samples/tcc/action/TccActionTwo.java) ;分布式事务提交则两者均提交，分布式事务回滚则两者均回滚；

这2个TCC参与者均是 本地bean（非远程RPC服务），本地TCC bean需要在 接口定义中添加 @LocalTCC 注解，其定义如下：

- TccActionOne 接口定义如下：

```java
@LocalTCC
public interface TccActionOne {

    @TwoPhaseBusinessAction(name = "TccActionOne" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, int a);

    public boolean commit(BusinessActionContext actionContext);

    public boolean rollback(BusinessActionContext actionContext);
}

```

- TccActionTwo 接口定义如下：

```java
@LocalTCC
public interface TccActionTwo {

    @TwoPhaseBusinessAction(name = "TccActionTwo" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, String b);

    public boolean commit(BusinessActionContext actionContext);

    public boolean rollback(BusinessActionContext actionContext);
}
```


## demo 运行指南

#### step 1 启动 Fesacr Server

运行 [SeataServerStarter](https://github.com/seata/seata-samples/blob/master/tcc/local-tcc-sample/src/test/java/io/seata/samples/tcc/SeataServerStarter.java) ，启动 Seata Server；

#### step 2 启动 TCC Demo

运行 [LocalTccTransactionStarter](https://github.com/seata/seata-samples/blob/master/tcc/local-tcc-sample/src/main/java/io/seata/samples/tcc/starter/LocalTccTransactionStarter.java) , 启动 demo工程；

内含TCC 事务提交场景和TCC事务回滚场景的演示；