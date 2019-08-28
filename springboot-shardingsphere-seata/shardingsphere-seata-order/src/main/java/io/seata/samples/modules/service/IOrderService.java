package io.seata.samples.modules.service;

import com.baomidou.mybatisplus.service.IService;
import io.seata.samples.modules.entity.OrderEntity;


public interface IOrderService extends IService<OrderEntity> {
    void insertOrder(OrderEntity orderEntity);
}
