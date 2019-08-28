package io.seata.samples.modules.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import io.seata.core.context.RootContext;
import io.seata.samples.modules.entity.OrderEntity;
import io.seata.samples.modules.service.IBusinessService;
import io.seata.samples.modules.service.IOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements IBusinessService {

    @Reference(mock = "io.seata.samples.modules.mock.IOrderServiceMock")
    IOrderService orderService;

    @Override
    @GlobalTransactional(name = "dubbo-purchase")
    public void purchase(){
        TransactionTypeHolder.set(TransactionType.BASE);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(123);
        orderEntity.setStatus("seata");
        orderEntity.setUserId(123);
        orderService.insertOrder(orderEntity);
        System.out.println("XID:"+ RootContext.getXID());
        throw new RuntimeException("回滚测试");
    }
}
