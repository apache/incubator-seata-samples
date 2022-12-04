/*
 *  Copyright 1999-2022 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.samples.service.impl;

import java.math.BigDecimal;
import java.util.List;

import io.seata.core.context.RootContext;
import io.seata.samples.bean.Order;
import io.seata.samples.bean.Stock;
import io.seata.samples.mapper.OrderMapper;
import io.seata.samples.mapper.StockMapper;
import io.seata.samples.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final StockMapper stockMapper;

    public OrderServiceImpl(OrderMapper orderMapper, StockMapper stockMapper) {
        this.orderMapper = orderMapper;
        this.stockMapper = stockMapper;
    }

    @Transactional
    @Override
    public Long createOrder(Long accountId, Long stockId, Long quantity) {
        Example example = new Example(Stock.class);
        example.setForUpdate(true);
        example.createCriteria().andEqualTo("id", stockId);
        Stock stock = this.stockMapper.selectOneByExample(example);
        if (stock == null) {
            throw new RuntimeException("stock can't be found,please check stock id");
        }
        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("insufficient stock quantity");
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        this.stockMapper.updateByPrimaryKeySelective(stock);
        BigDecimal amount = stock.getPrice().multiply(BigDecimal.valueOf(quantity));

        Order order = new Order();
        order.setAccountId(accountId);
        order.setAmount(amount);
        order.setQuantity(quantity);
        order.setStockId(stockId);
        this.orderMapper.insertSelective(order);


        return order.getId();
    }

    @Override
    public Boolean updateOrder(Long accountId, Long orderId, Long stockId, Long quantity) {
        return this.orderMapper.updateOrder(accountId, orderId, stockId, quantity) > 0;
    }

    @Override
    public Integer createOrUpdateOrder(Long id, Long orderNumber, Long accountId, Long stockId, Long quantity, String note) {
        return this.orderMapper.createOrUpdateOrder(id, orderNumber, accountId, stockId, quantity, note);
    }

    @Override
    public Integer createOrUpdateOrder2(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note) {
        return this.orderMapper.createOrUpdateOrder2(id, orderNumber, accountId, stockId, quantity, amount, note);
    }


    @Override
    public Integer createOrUpdateBatchOrder(List<Order> orders) {
        return orderMapper.createOrUpdateBatchOrder(orders);
    }

}
