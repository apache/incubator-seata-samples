package io.seata.samples.shardingsphere.modules.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.seata.samples.shardingsphere.modules.entity.OrderEntity;
import io.seata.samples.shardingsphere.modules.mapper.OrderMapper;
import io.seata.samples.shardingsphere.modules.service.IOrderService;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements IOrderService {

    @Override
    public void insertOrder(OrderEntity orderEntity) {
        baseMapper.insert(orderEntity);
    }
}
