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
package io.seata.samples.controller;

import io.seata.samples.service.StockService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/stock")
public class StockController {


    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }


    @PostMapping("/addOrUpdateStockFail")
    public String insertOrUpdate(@RequestParam BigDecimal quantity, @RequestParam BigDecimal price) {
        this.stockService.addOrUpdateStockFail(quantity, price);
        return null;
    }


    @PostMapping("/addOrUpdateStockSuccess")
    public Integer insertOrUpdate(@RequestParam Long stockId, @RequestParam BigDecimal quantity, @RequestParam BigDecimal price) {
        return this.stockService.addOrUpdateStockSuccess(stockId, quantity, price);
    }
}
