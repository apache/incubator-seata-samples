package io.nutz.demo.dubbo.rpc.service;

import io.nutz.demo.bean.Order;

public interface OrderService {

    /**
     * create order
     */
    Order create(String userId, String commodityCode, int orderCount);
}