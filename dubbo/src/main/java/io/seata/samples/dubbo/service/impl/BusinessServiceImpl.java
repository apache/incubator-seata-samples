/*
 *  Copyright 1999-2021 Seata.io Group.
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
package io.seata.samples.dubbo.service.impl;

import java.util.Random;

import io.seata.core.context.RootContext;
import io.seata.samples.dubbo.service.BusinessService;
import io.seata.samples.dubbo.service.OrderService;
import io.seata.samples.dubbo.service.StockService;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Please add the follow VM arguments:
 * <pre>
 *     -Djava.net.preferIPv4Stack=true
 * </pre>
 */
public class BusinessServiceImpl implements BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    private StockService stockService;
    private OrderService orderService;
    private Random random = new Random();

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    public void purchase(String userId, String commodityCode, int orderCount) {
        LOGGER.info("purchase begin ... xid: " + RootContext.getXID());
        stockService.deduct(commodityCode, orderCount);
        // just test batch update
        //stockService.batchDeduct(commodityCode, orderCount);
        orderService.create(userId, commodityCode, orderCount);
        if (random.nextBoolean()) {
            throw new RuntimeException("random exception mock!");
        }

    }

    /**
     * Sets stock service.
     *
     * @param stockService the stock service
     */
    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * Sets order service.
     *
     * @param orderService the order service
     */
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

}
