package io.seata.samples.modules.mock;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.seata.samples.modules.entity.OrderEntity;
import io.seata.samples.modules.mapper.OrderMapper;
import io.seata.samples.modules.service.IOrderService;

public class IOrderServiceMock extends ServiceImpl<OrderMapper, OrderEntity> implements IOrderService {
    @Override
    public void insertOrder(OrderEntity orderEntity) {
        System.out.println("OrderService RpcException");
    }
}
