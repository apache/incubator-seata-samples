# Seata TCC 模式 demo 工程


[Seata](https://github.com/seata/seata) 是一款开源的分布式事务解决方案，提供高性能和简单易用的分布式事务服务。   


## demo 说明

基于 Seata TCC模式，以转账场景为例，演示分布式事务的提交和回滚；

本demo中一个分布式事务内会有2个TCC事务参与者，分别是: [FirstTccAction](https://github.com/seata/seata-samples/blob/master/tcc/transfer-tcc-sample/src/main/java/io/seata/samples/tcc/transfer/action/FirstTccAction.java) 和 [SecondTccAction](https://github.com/seata/seata-samples/blob/master/tcc/transfer-tcc-sample/src/main/java/io/seata/samples/tcc/transfer/action/SecondTccAction.java) ；

FirstTccAction 实现了转账服务中的扣款TCC服务，SecondTccAction 实现了转账服务中的存款TCC服务；

这2个TCC参与者均是 dubbo远程服务，一个应用（TransferProviderStarter）实现TCC参与者，并将其发布成 <dubbo:service />；

另外一个事务发起方应用（TransferApplication），订阅TCC dubbo服务，调用 <dubbo:reference /> 编排TCC参与者，执行远程TCC服务；

- FirstTccAction 实现了扣款TCC服务，其接口定义如下：

```java
public interface FirstTccAction {
	
    @TwoPhaseBusinessAction(name = "firstTccAction", commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare_minus(BusinessActionContext businessActionContext,
                                 @BusinessActionContextParameter(paramName = "accountNo") String accountNo,
                                 @BusinessActionContextParameter(paramName = "amount") double amount);

    public boolean commit(BusinessActionContext businessActionContext);

    public boolean rollback(BusinessActionContext businessActionContext);
}

```


- SecondTccAction 实现了存款TCC服务，其接口定义如下：

```java
public interface SecondTccAction {

    @TwoPhaseBusinessAction(name = "secondTccAction", commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare_add(BusinessActionContext businessActionContext,
                               @BusinessActionContextParameter(paramName = "accountNo") String accountNo,
                               @BusinessActionContextParameter(paramName = "amount") double amount);

    public boolean commit(BusinessActionContext businessActionContext);

    public boolean rollback(BusinessActionContext businessActionContext);
}

```


## demo 运行指南

#### step 1 启动 Seata Server

运行 [SeataServerStarter](https://github.com/seata/seata-samples/blob/master/tcc/transfer-tcc-sample/src/test/java/io/seata/samples/tcc/SeataServerStarter.java) ，启动 Seata Server；


#### step 2 启动 dubbo 服务

运行 [TransferProviderStarter](https://github.com/seata/seata-samples/blob/master/tcc/transfer-tcc-sample/src/main/java/io/seata/samples/tcc/transfer/starter/TransferProviderStarter.java)

本应用发布TCC dubbo服务；

#### step 2 启动 TCC 转账 Demo

运行 [TransferApplication](https://github.com/seata/seata-samples/blob/master/tcc/transfer-tcc-sample/src/main/java/io/seata/samples/tcc/transfer/starter/TransferApplication.java) , 启动 demo工程；

本应用订阅TCC dubbo服务，发起方分布式事务，调用TCC dubbo参与者，完成转账；内含转账事务提交（doTransferSuccess）场景和转账事务回滚（doTransferFailed）场景的演示；