package io.seata.samples.modules.service;

import com.baomidou.mybatisplus.service.IService;
import io.seata.samples.modules.entity.OrderItemEntity;


public interface IOrderItemService extends IService<OrderItemEntity> {
    void insertOrderItem(OrderItemEntity orderItemEntity);
}
