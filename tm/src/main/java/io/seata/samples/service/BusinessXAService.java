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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import io.seata.core.context.RootContext;
import io.seata.samples.bean.Stock;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class BusinessXAService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessXAService.class);

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
        Map<String, Object> params = new HashMap<>(8);
        params.put("accountId", accountId);
        params.put("money", amount);
        return JSON.parseObject(HttpRequest.post("http://127.0.0.1:8082/api/account/reduce").form(params)
            .header(RootContext.KEY_XID, RootContext.getXID()).execute().body(), Boolean.class);
    }

    private Stock getStock(Long stockId) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("stockId", stockId);
        return JSON.parseObject(HttpRequest.get("http://127.0.0.1:8081/api/order/queryStock")
            .form(params)
            .header(RootContext.KEY_XID, RootContext.getXID()).execute().body(), Stock.class);
    }

    private Long createOrder(long accountId, long stockId, long quantity) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("accountId", accountId);
        params.put("stockId", stockId);
        params.put("quantity", quantity);
        return JSON.parseObject(HttpRequest.post("http://127.0.0.1:8081/api/order/create").form(params)
            .header(RootContext.KEY_XID, RootContext.getXID()).execute().body(), Long.class);
    }
}
