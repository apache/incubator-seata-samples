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
package io.seata.samples.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import io.seata.core.context.RootContext;
import io.seata.samples.bean.Account;
import io.seata.samples.bean.Stock;
import io.seata.samples.mapper.AccountMapper;
import io.seata.samples.service.BuyService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class BuyServiceImpl implements BuyService {
    private final AccountMapper accountMapper;

    public BuyServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @GlobalTransactional(name = "re")
    @Override
    public Boolean placeOrder(Long accountId, Long stockId, Long quantity, boolean success) {
        if (Objects.isNull(accountId) || Objects.isNull(stockId) || Objects.isNull(quantity)) {
            throw new IllegalArgumentException("required parameters should not be null");
        }

        Stock stock = getStock(stockId);
        if (stock == null) {
            throw new RuntimeException("stock can't be found,please check stock id");
        }
        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("insufficient stock quantity");
        }
        BigDecimal amount = stock.getPrice().multiply(BigDecimal.valueOf(quantity));

        Example example = new Example(Account.class);
        example.setForUpdate(true);
        example.createCriteria()
            .andEqualTo("id", accountId);
        Account account = this.accountMapper.selectOneByExample(example);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        this.accountMapper.updateByPrimaryKeySelective(account);


        Long orderId = createOrder(accountId, stockId, quantity);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return orderId != null;
    }

    @GlobalTransactional
    @Override
    public Boolean updateOrder(Long accountId, Long stockId, Long quantity, Long orderId, boolean success) {
        if (Objects.isNull(accountId) || Objects.isNull(stockId) || Objects.isNull(quantity) || Objects.isNull(orderId)) {
            throw new IllegalArgumentException("required parameters should not be null");
        }

        Map<String, Object> params = new HashMap<>(8);
        params.put("accountId", accountId);
        params.put("stockId", stockId);
        params.put("quantity", quantity);
        params.put("orderId", orderId);
        Boolean result = JSON.parseObject(HttpRequest.post("http://127.0.0.1:8081/api/order/update")
            .form(params)
            .header(RootContext.KEY_XID, RootContext.getXID()).execute().body(), Boolean.class);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    private Stock getStock(Long stockId) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("stockId", stockId);
        return JSON.parseObject(HttpRequest.get("http://127.0.0.1:8081/api/order/queryStock")
            .form(params)
            .header(RootContext.KEY_XID, RootContext.getXID()).execute().body(), Stock.class);
    }

    private Long createOrder(Long accountId, Long stockId, Long quantity) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("accountId", accountId);
        params.put("stockId", stockId);
        params.put("quantity", quantity);
        return JSON.parseObject(HttpRequest.post("http://127.0.0.1:8081/api/order/create")
            .form(params)
            .header(RootContext.KEY_XID, RootContext.getXID()).execute().body(), Long.class);
    }
}
