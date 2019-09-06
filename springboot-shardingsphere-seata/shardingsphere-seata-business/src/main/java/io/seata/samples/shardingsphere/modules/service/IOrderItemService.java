package io.seata.samples.shardingsphere.modules.service;

import com.baomidou.mybatisplus.service.IService;
import io.seata.samples.shardingsphere.modules.entity.OrderItemEntity;


public interface IOrderItemService extends IService<OrderItemEntity> {
    void insertOrderItem(OrderItemEntity orderItemEntity);
}
