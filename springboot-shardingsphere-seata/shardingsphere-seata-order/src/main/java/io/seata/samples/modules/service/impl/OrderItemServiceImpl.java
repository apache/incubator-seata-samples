package io.seata.samples.modules.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.seata.samples.modules.entity.OrderItemEntity;
import io.seata.samples.modules.mapper.OrderItemMapper;
import io.seata.samples.modules.service.IOrderItemService;


public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItemEntity> implements IOrderItemService {

    @Override
    public void insertOrderItem(OrderItemEntity orderItemEntity) {
        baseMapper.insert(orderItemEntity);
    }
}
