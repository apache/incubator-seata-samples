# Seata Saga 模式 demo 工程


[Seata](https://github.com/seata/seata) 是一款开源的分布式事务解决方案，提供高性能和简单易用的分布式事务服务。


基于 Seata Saga模式，演示分布式事务的提交和回滚；

本demo中一个分布式事务内会有2个Saga事务参与者，分别是: [InventoryAction](https://github.com/seata/seata-samples/blob/master/saga/dubbo-saga-sample/src/main/java/io/seata/samples/saga/action/InventoryAction.java) 和 [BalanceAction](https://github.com/seata/seata-samples/blob/master/saga/dubbo-saga-sample/src/main/java/io/seata/samples/saga/action/BalanceAction.java) ;分布式事务提交则两者均提交，分布式事务回滚则两者均回滚；

这2个Saga参与者均是 dubbo 服务，两个参与都有一个reduce方法，表示库存扣减或余额扣减，还有一个compensateReduce方法，表示补偿扣减操作。

- InventoryAction 接口定义如下：

```java
public interface InventoryAction {

    /**
     * reduce
     * @param businessKey
     * @param amount
     * @param params
     * @return
     */
    boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params);

    /**
     * compensateReduce
     * @param businessKey
     * @param params
     * @return
     */
    boolean compensateReduce(String businessKey, Map<String, Object> params);
}
```


## demo 运行指南

#### step 1 启动 SEATA Server

运行 [SeataServerStarter](https://github.com/seata/seata-samples/blob/master/saga/sofarpc-saga-sample/src/test/java/io/seata/samples/saga/SeataServerStarter.java) ，启动 Seata Server；

#### step 2 启动 dubbo provider Demo

运行 [DubboSagaProviderStarter](https://github.com/seata/seata-samples/blob/master/saga/dubbo-saga-sample/src/test/java/io/seata/samples/saga/starter/DubboSagaProviderStarter.java) ，启动 dubbo provider；

### step 3 启动 Saga Demo

运行 [DubboSagaTransactionStarter](https://github.com/seata/seata-samples/blob/master/saga/dubbo-saga-sample/src/main/java/io/seata/samples/saga/starter/DubboSagaTransactionStarter.java) , 启动 demo工程；


内含Saga 调用dubbo服务 事务提交场景和Saga 调用dubbo服务 事务回滚场景的演示；