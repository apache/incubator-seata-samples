package com.seata.order.controller;


import com.seata.order.model.Order;
import com.seata.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("order/create")
    public Boolean create(@RequestBody Order order){

        return orderService.create(order);
    }
}
