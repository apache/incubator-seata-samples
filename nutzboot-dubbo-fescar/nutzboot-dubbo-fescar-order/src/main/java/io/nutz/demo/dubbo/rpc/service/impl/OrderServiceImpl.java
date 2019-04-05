package io.nutz.demo.dubbo.rpc.service.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;

import io.nutz.demo.bean.Order;
import io.nutz.demo.dubbo.rpc.service.AccountService;
import io.nutz.demo.dubbo.rpc.service.OrderService;

@IocBean
@Service(interfaceClass=OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Inject
    private Dao dao;

    @Inject
    @Reference
    private AccountService accountService;

    public Order create(String userId, String commodityCode, int orderCount) {

        int orderMoney = calculate(commodityCode, orderCount);

        accountService.debit(userId, orderMoney);

        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(orderCount);
        order.setMoney(orderMoney);

        // INSERT INTO orders ...
        return dao.insert(order);
    }
    
    protected int calculate(String commodityCode, int orderCount) {
        return 100;
    }
}