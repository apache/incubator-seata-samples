package com.seata.business.controller;

import com.seata.business.client.AccountClient;
import com.seata.business.client.InventoryClient;
import com.seata.business.client.OrderClient;
import com.seata.business.model.Order;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private InventoryClient inventoryClient;
    @Autowired
    private AccountClient accountClient;

    @GetMapping("business/buy")
    @GlobalTransactional
    public String buy( String userId, String commodityCode, int count){
        int money = count * 1;
        accountClient.debit(userId,money);
        Order order  = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(money);
        orderClient.create(order);
        inventoryClient.deduct(commodityCode,count);
        return "success";
    }

}
