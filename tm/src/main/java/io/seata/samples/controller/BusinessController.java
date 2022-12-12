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

import io.seata.samples.bean.Order;
import io.seata.samples.service.BuyService;
import io.seata.samples.service.BusinessXAService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/buy")
public class BusinessController {
    @Resource
    private BusinessXAService businessService;

    @Resource
    private BuyService buyService;


    @RequestMapping(value = "/purchase", method = RequestMethod.GET, produces = "application/json")
    public boolean purchase(@RequestParam long accountId,@RequestParam long stockId) {
        return businessService.purchase(accountId, stockId, 1);
    }

    @PostMapping("/increaseAccountMoney")
    public Boolean increaseAccountMoney(@RequestParam Long accountId, @RequestParam BigDecimal money) {
        return this.buyService.increaseAccountMoney(accountId,money);
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
    public Integer createOrUpdateOrderSuccess(@RequestParam Long id, @RequestParam Long accountId, @RequestParam Long orderNumber, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam(required = false) BigDecimal amount, @RequestParam(required = false) String note, HttpServletRequest request) {
        return this.buyService.createOrUpdateOrder(id, accountId, orderNumber, stockId, quantity, amount, note, true);
    }

    @PostMapping("/createOrUpdateOrderFail")
    public Integer createOrUpdateOrderFail(@RequestParam Long id, @RequestParam Long accountId, @RequestParam Long orderNumber, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam(required = false) BigDecimal amount, @RequestParam(required = false) String note, HttpServletRequest request) {
        return this.buyService.createOrUpdateOrder(id, accountId, orderNumber, stockId, quantity, amount, note, false);
    }

    @PostMapping("/createOrUpdateOrderSuccess2")
    public Integer createOrUpdateOrderSuccess2(@RequestParam Long id, @RequestParam Long accountId, @RequestParam Long orderNumber, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam BigDecimal amount, @RequestParam String note, HttpServletRequest request) {
        return this.buyService.createOrUpdateOrder2(id, accountId, orderNumber, stockId, quantity, amount, note, true);
    }

    @PostMapping("/createOrUpdateBatchOrderSuccess")
    public Integer createOrUpdateBatchOrderSuccess(@RequestBody List<Order> orders, HttpServletRequest request) {
        return this.buyService.createOrUpdateBatchOrderSuccess(orders, true);
    }

    @PostMapping("/createOrUpdateBatchOrderFail")
    public Integer createOrUpdateBatchOrderFail(@RequestBody List<Order> orders, HttpServletRequest request) {
        return this.buyService.createOrUpdateBatchOrderSuccess(orders, false);
    }

    @PostMapping("/addOrUpdateStockFail2")
    public String addOrUpdateStockFail2(@RequestParam BigDecimal quantity, @RequestParam BigDecimal price) {
        return buyService.addOrUpdateStockFail(quantity, price, true);
    }

    @PostMapping("/addOrUpdateStockSuccess")
    public Integer addOrUpdateStockSuccess2(@RequestParam Long stockId, @RequestParam BigDecimal quantity, @RequestParam BigDecimal price) {
        return buyService.addOrUpdateStock(stockId, quantity, price, true);
    }

    @PostMapping("/addOrUpdateStockFail")
    public Integer addOrUpdateStockFail(@RequestParam Long stockId, @RequestParam BigDecimal quantity, @RequestParam BigDecimal price) {
        return buyService.addOrUpdateStock(stockId, quantity, price, false);
    }
}
