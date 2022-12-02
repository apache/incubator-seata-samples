package io.seata.samples.xa.controller;

import io.seata.samples.xa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/xa/order/create")
    public Boolean create(@RequestParam Long accountId, @RequestParam Long stockId, @RequestParam Long quantity) {
        return this.orderService.createOrder(accountId, stockId, quantity);
    }

}
