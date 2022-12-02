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

import java.math.BigDecimal;

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

    @PostMapping("/createOrUpdate")
    public Integer insertOrUpdate(@RequestParam Long id, @RequestParam Long accountId, @RequestParam Long orderNumber, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam(required = false) BigDecimal amount, @RequestParam String note, HttpServletRequest request) {
        return this.orderService.createOrUpdateOrder(id, accountId, orderNumber, stockId, quantity, note);
    }

    @PostMapping("/createOrUpdate2")
    public Integer createOrUpdateOrder2(@RequestParam Long id, @RequestParam Long accountId, @RequestParam Long orderNumber, @RequestParam Long stockId, @RequestParam Long quantity, @RequestParam BigDecimal amount, String note, HttpServletRequest request) {
        return this.orderService.createOrUpdateOrder2(id, accountId, orderNumber, stockId, quantity, amount, note);
    }

    @PostMapping("/addOrUpdateStock")
    public Boolean insertOrUpdate(@RequestParam BigDecimal quantity, @RequestParam BigDecimal price) {
        return this.orderService.addOrUpdateStock(quantity, price);
    }
}
