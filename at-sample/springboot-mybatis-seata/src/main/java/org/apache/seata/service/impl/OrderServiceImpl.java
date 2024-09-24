/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.seata.service.impl;

import org.apache.seata.core.context.RootContext;
import org.apache.seata.dao.OrderMapper;
import org.apache.seata.model.Order;
import org.apache.seata.service.AccountService;
import org.apache.seata.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * The type Order service.
 *
 * @author jimin.jm @alibaba-inc.com
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Resource
    private AccountService accountService;
    @Resource
    private OrderMapper orderMapper;

    @Override
    public void create(String userId, String commodityCode, int count) {
        LOGGER.info("Order Service Begin ... xid: " + RootContext.getXID());
        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        int orderMoney = calculate(commodityCode, count);
        order.setMoney(orderMoney);

        orderMapper.insert(order);
        accountService.debit(userId, orderMoney);
    }

    private int calculate(String commodityCode, int count) {
        return 200 * count;
    }

}
