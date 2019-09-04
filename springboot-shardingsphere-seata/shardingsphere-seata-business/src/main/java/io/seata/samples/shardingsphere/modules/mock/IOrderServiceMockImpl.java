package io.seata.samples.shardingsphere.modules.mock;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.seata.samples.shardingsphere.modules.entity.OrderEntity;
import io.seata.samples.shardingsphere.modules.mapper.OrderMapper;
import io.seata.samples.shardingsphere.modules.service.IOrderService;

public class IOrderServiceMockImpl extends ServiceImpl<OrderMapper, OrderEntity> implements IOrderService {
    @Override
    public void insertOrder(OrderEntity orderEntity) {
        System.out.println("OrderService RpcException");
    }
}
