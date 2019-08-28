package io.seata.samples.shardingsphere.modules.service;

import com.baomidou.mybatisplus.service.IService;
import io.seata.samples.shardingsphere.modules.entity.OrderEntity;


public interface IOrderService extends IService<OrderEntity> {
    void insertOrder(OrderEntity orderEntity);
}
