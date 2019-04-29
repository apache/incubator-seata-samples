# Seata TCC 模式 demo 工程


[Seata](https://github.com/seata/seata) 是一款开源的分布式事务解决方案，提供高性能和简单易用的分布式事务服务。   


## demo 说明

基于 Seata TCC模式，演示分布式事务的提交和回滚；

本demo中一个分布式事务内会有2个TCC事务参与者，分别是: [TccActionOne](https://github.com/seata/seata-samples/blob/master/tcc/sofarpc-tcc-sample/src/main/java/io/seata/samples/tcc/sofarpc/action/TccActionOne.java) 和 [TccActionTwo](https://github.com/seata/seata-samples/blob/master/tcc/sofarpc-tcc-sample/src/main/java/io/seata/samples/tcc/sofarpc/action/TccActionTwo.java) ;分布式事务提交则两者均提交，分布式事务回滚则两者均回滚；

这2个TCC参与者均是 SofaRpc 远程服务，一个应用实现TCC参与者，并将其发布成 <sofa:service />；

另外一个事务发起方应用，订阅TCC sofa 服务，调用 <sofa:reference /> 编排TCC参与者，执行远程TCC服务；其接口定义如下：

- TccActionOne 接口定义如下：

```java
public interface TccActionOne {

    @TwoPhaseBusinessAction(name = "SofaRpcTccActionOne" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, int a);

    public boolean commit(BusinessActionContext actionContext);

    public boolean rollback(BusinessActionContext actionContext);
}
```


- TccActionTwo 接口定义如下：

```java
public interface TccActionTwo {

    @TwoPhaseBusinessAction(name = "SofaRpcTccActionTwo" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, String b);

    public boolean commit(BusinessActionContext actionContext);

    public boolean rollback(BusinessActionContext actionContext);

}
```


## demo 运行指南

#### step 1 启动 Seata Server

运行 [SeataServerStarter](https://github.com/seata/seata-samples/blob/master/tcc/sofarpc-tcc-sample/src/test/java/io/seata/samples/tcc/server/SeataServerStarter.java) ，启动 Seata Server；


#### step 2 启动 sofarpc 服务

运行 [SofaRpcTccProviderApplication](https://github.com/seata/seata-samples/blob/master/tcc/sofarpc-tcc-sample/src/test/java/io/seata/samples/tcc/provider/SofaRpcTccProviderApplication.java)

本应用发布TCC sofa-rpc 服务；

#### step 2 启动 TCC Demo

运行 [SofaRPCTccTransactionApplication](https://github.com/seata/seata-samples/blob/master/tcc/sofarpc-tcc-sample/src/main/java/io/seata/samples/tcc/sofarpc/starter/SofaRPCTccTransactionApplication.java) , 启动 demo工程；

本应用订阅TCC sofa-rpc 服务，发起方分布式事务，调用TCC sofa-rpc 参与者；内含TCC 事务提交场景和TCC事务回滚场景的演示；