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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.seata.core.context.RootContext;
import io.seata.samples.bean.Account;
import io.seata.samples.bean.Order;
import io.seata.samples.bean.Stock;
import io.seata.samples.mapper.AccountMapper;
import io.seata.samples.service.BuyService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

@Service
public class BuyServiceImpl implements BuyService {
    private final AccountMapper accountMapper;
    private final RestTemplate restTemplate;

    public BuyServiceImpl(AccountMapper accountMapper,RestTemplate restTemplate) {
        this.accountMapper = accountMapper;
        this.restTemplate = restTemplate;
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


    @Override
    @GlobalTransactional
    public Integer createOrUpdateOrder(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note, boolean success) {
        String url = "http://127.0.0.1:8081/api/order/createOrUpdate";
        String body = callService(url, id, accountId, orderNumber, stockId, quantity, amount, note);
        Integer result = JSON.parseObject(body, Integer.class);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    @GlobalTransactional
    @Override
    public Boolean createOrUpdateOrder2(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note, boolean success) {
        String url = "http://127.0.0.1:8081/api/order/createOrUpdate2";
        String body = callService(url, id, accountId, orderNumber, stockId, quantity, amount, note);
        Boolean result = JSON.parseObject(body, Boolean.class);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }


    @GlobalTransactional
    @Override
    public Integer createOrUpdateBatchOrderSuccess(List<Order> orders, boolean success) {
        String json = JSONObject.toJSONString(orders);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add(RootContext.KEY_XID, RootContext.getXID());

        HttpEntity<String> entity = new HttpEntity<>(json, header);
        ResponseEntity<Integer> result = restTemplate.postForEntity("http://localhost:8081/api/order/createOrUpdateBatch", entity, Integer.class);
        Integer rows = result.getBody();
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return rows;
    }

    @GlobalTransactional
    @Override
    public Boolean addOrUpdateStock(BigDecimal quantity, BigDecimal price, boolean success) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("price", price);
        params.put("quantity", quantity);
        String body = HttpRequest.post("http://127.0.0.1:8081/api/stock/addOrUpdateStock")
                .form(params)
                .header(RootContext.KEY_XID, RootContext.getXID()).execute().body();
        Boolean result = JSON.parseObject(body, Boolean.class);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    @GlobalTransactional
    @Override
    public Boolean addOrUpdateStock2(Long stockId, BigDecimal quantity, BigDecimal price, boolean success) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("stockId", stockId);
        params.put("price", price);
        params.put("quantity", quantity);
        String body = HttpRequest.post("http://127.0.0.1:8081/api/stock/insertOrUpdateStock2")
                .form(params)
                .header(RootContext.KEY_XID, RootContext.getXID()).execute().body();
        Boolean result = JSON.parseObject(body, Boolean.class);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    private String callService(String url, Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("id", id);
        params.put("accountId", accountId);
        params.put("orderNumber", orderNumber);
        params.put("stockId", stockId);
        params.put("quantity", quantity);
        params.put("note", note);
        params.put("amount", amount);
        String body = HttpRequest.post(url)
                .form(params)
                .header(RootContext.KEY_XID, RootContext.getXID()).execute().body();
        return body;
    }
}
