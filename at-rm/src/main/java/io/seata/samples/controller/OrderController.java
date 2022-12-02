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
package io.seata.samples.controller;

import javax.servlet.http.HttpServletRequest;

import io.seata.samples.bean.Stock;
import io.seata.samples.service.OrderService;
import io.seata.samples.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final StockService stockService;

    public OrderController(OrderService orderService, StockService stockService) {
        this.orderService = orderService;
        this.stockService = stockService;
    }


    @PostMapping("/create")
    public Long create(@RequestParam Long accountId, @RequestParam Long stockId, @RequestParam Long quantity, HttpServletRequest request) {
        return this.orderService.createOrder(accountId, stockId, quantity);
    }


    @PostMapping("/update")
    public Boolean update(@RequestParam Long accountId, @RequestParam Long orderId, @RequestParam Long stockId, @RequestParam Long quantity, HttpServletRequest request) {
        return this.orderService.updateOrder(accountId, orderId, stockId, quantity);
    }

    @GetMapping("/queryStock")
    public Stock queryStock(@RequestParam Long stockId, HttpServletRequest request) {
        return this.stockService.getStockById(stockId);
    }


}
