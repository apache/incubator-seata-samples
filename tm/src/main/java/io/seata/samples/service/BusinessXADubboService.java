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
package io.seata.samples.service;

import io.seata.core.context.RootContext;
import io.seata.samples.bean.Stock;
import io.seata.samples.client.service.AccountService;
import io.seata.samples.client.service.OrderService;
import io.seata.samples.client.service.StockService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BusinessXADubboService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessXADubboService.class);

    @DubboReference
    private OrderService orderService;

    @DubboReference
    private StockService stockService;

    @DubboReference
    private AccountService accountService;

    @GlobalTransactional
    public boolean purchase(long accountId, long stockId, long quantity) {
        String xid = RootContext.getXID();
        LOGGER.info("New Transaction Begins: " + xid);
        boolean stockResult = reduceAccount(accountId,stockId, quantity);
        if (!stockResult) {
            throw new RuntimeException("账号服务调用失败,事务回滚!");
        }
        Long orderId = createOrder(accountId, stockId, quantity);
        if (orderId == null || orderId <= 0) {
            throw new RuntimeException("订单服务调用失败,事务回滚!");
        }
        return true;
    }

    private boolean reduceAccount(long accountId,long stockId, long quantity) {
        Stock stock = getStock(stockId);
        if (stock == null) {
            throw new RuntimeException("stock can't be found,please check stock id");
        }
        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("insufficient stock quantity");
        }
        BigDecimal amount = stock.getPrice().multiply(BigDecimal.valueOf(quantity));

        return accountService.reduce(accountId,amount);
    }

    private Stock getStock(Long stockId) {
        return stockService.getStockById(stockId);
    }

    private Long createOrder(long accountId, long stockId, long quantity) {
        return orderService.createOrder(accountId, stockId, quantity);
    }
}
