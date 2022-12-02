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

import javax.servlet.http.HttpServletRequest;

import io.seata.samples.service.BuyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/buy")
public class BuyController {
    private final BuyService buyService;

    public BuyController(BuyService buyService) {
        this.buyService = buyService;
    }


    @PostMapping("/placeOrderSuccess")
    public Boolean placeOrderSuccess(@RequestParam Long accountId, @RequestParam Long stockId, @RequestParam Long quantity, HttpServletRequest request) {
        return this.buyService.placeOrder(accountId, stockId, quantity, true);
    }

    @PostMapping("/placeOrderFail")
    public Boolean placeOrderFail(@RequestParam Long accountId, @RequestParam Long stockId, @RequestParam Long quantity, HttpServletRequest request) {
        return this.buyService.placeOrder(accountId, stockId, quantity, false);
    }

    @PostMapping("/updateOrderSuccess")
    public Boolean updateOrderSuccess(@RequestParam Long accountId, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam Long orderId, HttpServletRequest request) {
        return this.buyService.updateOrder(accountId, stockId, quantity, orderId, true);
    }

    @PostMapping("/updateOrderFail")
    public Boolean updateOrderFail(@RequestParam Long accountId, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam Long orderId, HttpServletRequest request) {
        return this.buyService.updateOrder(accountId, stockId, quantity, orderId, false);
    }

    @PostMapping("/createOrUpdateOrderSuccess")
    public Integer createOrUpdateOrderSuccess(@RequestParam Long id, @RequestParam Long accountId, @RequestParam Long orderNumber, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam(required = false) BigDecimal amount, @RequestParam String note, HttpServletRequest request) {
        return this.buyService.createOrUpdateOrder(id, accountId, orderNumber, stockId, quantity, amount, note, true);
    }

    @PostMapping("/createOrUpdateOrderFail")
    public Integer createOrUpdateOrderFail(@RequestParam Long id, @RequestParam Long accountId, @RequestParam Long orderNumber, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam(required = false) BigDecimal amount, @RequestParam String note, HttpServletRequest request) {
        return this.buyService.createOrUpdateOrder(id, accountId, orderNumber, stockId, quantity, amount, note, false);
    }

    @PostMapping("/createOrUpdateOrderSuccess2")
    public Boolean createOrUpdateOrderSuccess2(@RequestParam Long id, @RequestParam Long accountId, @RequestParam Long orderNumber, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam BigDecimal amount, @RequestParam String note, HttpServletRequest request) {
        return this.buyService.createOrUpdateOrder2(id, accountId, orderNumber, stockId, quantity, amount, note, true);
    }

    @PostMapping("/addOrUpdateStockSuccess")
    public Boolean addOrUpdateStockSuccess(@RequestParam BigDecimal quantity, @RequestParam BigDecimal price) {
        return buyService.addOrUpdateStock(quantity, price, true);
    }
}
