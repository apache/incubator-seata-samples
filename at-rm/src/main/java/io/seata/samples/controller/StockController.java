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


    @PostMapping("/addOrUpdateStock")
    public Boolean insertOrUpdate(@RequestParam BigDecimal quantity, @RequestParam BigDecimal price) {
        return this.stockService.addOrUpdateStock(quantity, price);
    }


    @PostMapping("/addOrUpdateStock2")
    public Boolean insertOrUpdate(@RequestParam Long stockId, @RequestParam BigDecimal quantity, @RequestParam BigDecimal price) {
        return this.stockService.addOrUpdateStock2(stockId, quantity, price);
    }
}
