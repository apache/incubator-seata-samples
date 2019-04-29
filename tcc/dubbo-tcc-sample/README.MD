# Seata TCC 模式 demo 工程


[Seata](https://github.com/seata/seata) 是一款开源的分布式事务解决方案，提供高性能和简单易用的分布式事务服务。   


## demo 说明

基于 Seata TCC模式，演示分布式事务的提交和回滚；

本demo中一个分布式事务内会有2个TCC事务参与者，分别是: [TccActionOne](https://github.com/seata/seata-samples/blob/master/tcc/dubbo-tcc-sample/src/main/java/io/seata/samples/tcc/dubbo/action/TccActionOne.java) 和 [TccActionTwo](https://github.com/seata/seata-samples/blob/master/tcc/dubbo-tcc-sample/src/main/java/io/seata/samples/tcc/dubbo/action/TccActionTwo.java) ;分布式事务提交则两者均提交，分布式事务回滚则两者均回滚；

这2个TCC参与者均是 dubbo远程服务，一个应用实现TCC参与者，并将其发布成 <dubbo:service />；

另外一个事务发起方应用，订阅TCC dubbo服务，调用 <dubbo:reference /> 编排TCC参与者，执行远程TCC服务；其接口定义如下：

- TccActionOne 接口定义如下：

```java
public interface TccActionOne {

    @TwoPhaseBusinessAction(name = "DubboTccActionOne" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, int a);

    public boolean commit(BusinessActionContext actionContext);

    public boolean rollback(BusinessActionContext actionContext);
}
```


- TccActionTwo 接口定义如下：

```java
public interface TccActionTwo {

    @TwoPhaseBusinessAction(name = "DubboTccActionTwo" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, String b);

    public boolean commit(BusinessActionContext actionContext);

    public boolean rollback(BusinessActionContext actionContext);

}
```


## demo 运行指南

#### step 1 启动 Fesacr Server

运行 [SeataServerStarter](https://github.com/seata/seata-samples/blob/master/tcc/dubbo-tcc-sample/src/test/java/io/seata/samples/tcc/SeataServerStarter.java) ，启动 Seata Server；


#### step 2 启动 dubbo 服务

运行 [DubboTccProviderStarter](https://github.com/seata/seata-samples/blob/master/tcc/dubbo-tcc-sample/src/main/java/io/seata/samples/tcc/dubbo/starter/DubboTccProviderStarter.java)

本应用发布TCC dubbo服务；

#### step 2 启动 TCC Demo（）

运行 [DubboTccTransactionStarter](https://github.com/seata/seata-samples/blob/master/tcc/dubbo-tcc-sample/src/main/java/io/seata/samples/tcc/dubbo/starter/DubboTccTransactionStarter.java) , 启动 demo工程；

本应用订阅TCC dubbo服务，发起方分布式事务，调用TCC dubbo参与者；内含TCC 事务提交场景和TCC事务回滚场景的演示；