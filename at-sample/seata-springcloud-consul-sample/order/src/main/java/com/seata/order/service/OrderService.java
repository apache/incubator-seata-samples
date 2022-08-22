package com.seata.order.service;

import com.seata.order.model.Order;

public interface OrderService {

    boolean create(Order order);
}