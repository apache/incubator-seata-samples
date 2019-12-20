package io.seata.samples.shardingsphere.modules.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.seata.samples.shardingsphere.modules.entity.OrderItemEntity;
import io.seata.samples.shardingsphere.modules.mapper.OrderItemMapper;
import io.seata.samples.shardingsphere.modules.service.IOrderItemService;


public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItemEntity> implements IOrderItemService {

    @Override
    public void insertOrderItem(OrderItemEntity orderItemEntity) {
        baseMapper.insert(orderItemEntity);
    }
}
