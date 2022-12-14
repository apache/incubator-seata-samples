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

import io.seata.samples.bean.Order;
import io.seata.samples.client.service.AccountService;
import io.seata.samples.client.service.BuyService;
import io.seata.samples.client.service.OrderService;
import io.seata.samples.client.service.StockService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@DubboService
public class BuyServiceDubboImpl implements BuyService {

    @DubboReference
    private OrderService orderService;

    @DubboReference
    private AccountService accountService;

    @DubboReference
    private StockService stockService;

    @Override
    @GlobalTransactional
    public Boolean updateOrder(Long accountId, Long stockId, Long quantity, Long orderId, boolean success) {
        if (Objects.isNull(accountId) || Objects.isNull(stockId) || Objects.isNull(quantity) || Objects.isNull(orderId)) {
            throw new IllegalArgumentException("required parameters should not be null");
        }
        Boolean result =  this.orderService.updateOrder(accountId, orderId, stockId, quantity);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    @Override
    @GlobalTransactional
    public Integer createOrUpdateOrder(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note, boolean success) {
        Integer result = orderService.createOrUpdateOrder(id, accountId, orderNumber, stockId, quantity, note);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    @GlobalTransactional
    @Override
    public Integer createOrUpdateOrder2(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note, boolean success) {
        Integer result = this.orderService.createOrUpdateOrder2(id, accountId, orderNumber, stockId, quantity, amount, note);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    @GlobalTransactional
    @Override
    public Integer createOrUpdateBatchOrderSuccess(List<Order> orders, boolean success) {
        Integer rows = this.orderService.createOrUpdateBatchOrder(orders);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return rows;
    }

    @Override
    @GlobalTransactional
    public Boolean increaseAccountMoney(Long accountId, BigDecimal money) {
        Boolean result = this.accountService.increase(accountId, money);
        if (!result) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    @GlobalTransactional
    @Override
    public String addOrUpdateStockFail(BigDecimal quantity, BigDecimal price, boolean success) {
        stockService.addOrUpdateStockFail(quantity, price);

        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return null;
    }

    @GlobalTransactional
    @Override
    public Integer addOrUpdateStock(Long stockId, BigDecimal quantity, BigDecimal price, boolean success) {
       Integer result = stockService.addOrUpdateStockSuccess(stockId, quantity, price);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }
}
