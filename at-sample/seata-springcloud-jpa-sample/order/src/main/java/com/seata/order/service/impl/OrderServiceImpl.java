package com.seata.order.service.impl;


import com.seata.order.dao.OrderDAO;
import com.seata.order.model.Order;
import com.seata.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public boolean create(Order order) {
        log.info("创建订单开始");
        orderDAO.save(order);
        log.info("创建订单结束");
        return true;
    }
}
