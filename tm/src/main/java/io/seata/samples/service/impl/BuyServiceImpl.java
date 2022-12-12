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

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpRequest;
import io.seata.core.context.RootContext;
import io.seata.samples.bean.Order;
import io.seata.samples.bean.Stock;
import io.seata.samples.service.BuyService;
import io.seata.spring.annotation.GlobalTransactional;

@Service
public class BuyServiceImpl implements BuyService {
    private final RestTemplate restTemplate;

    public BuyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @GlobalTransactional
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
    public Integer createOrUpdateOrder2(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note, boolean success) {
        String url = "http://127.0.0.1:8081/api/order/createOrUpdate2";
        String body = callService(url, id, accountId, orderNumber, stockId, quantity, amount, note);
        Integer result = JSON.parseObject(body, Integer.class);
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

    @Override
    @GlobalTransactional
    public Boolean increaseAccountMoney(Long accountId, BigDecimal money) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("accountId", accountId);
        params.put("money", money);
        String body = HttpRequest.post("http://127.0.0.1:8082/api/account/increase").form(params)
                .header(RootContext.KEY_XID, RootContext.getXID()).execute().body();
        Boolean result = JSON.parseObject(body, Boolean.class);
        if (!result) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    @GlobalTransactional
    @Override
    public String addOrUpdateStockFail(BigDecimal quantity, BigDecimal price, boolean success) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("price", price);
        params.put("quantity", quantity);
        String body = HttpRequest.post("http://127.0.0.1:8081/api/stock/addOrUpdateStockFail")
                .form(params)
                .header(RootContext.KEY_XID, RootContext.getXID()).execute().body();
        String result = JSON.parseObject(body, String.class);
        if (!success) {
            throw new RuntimeException("testing roll back");
        }
        return result;
    }

    @GlobalTransactional
    @Override
    public Integer addOrUpdateStock(Long stockId, BigDecimal quantity, BigDecimal price, boolean success) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("stockId", stockId);
        params.put("price", price);
        params.put("quantity", quantity);
        String body = HttpRequest.post("http://127.0.0.1:8081/api/stock/addOrUpdateStockSuccess")
                .form(params)
                .header(RootContext.KEY_XID, RootContext.getXID()).execute().body();
        Integer result = JSON.parseObject(body, Integer.class);
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
