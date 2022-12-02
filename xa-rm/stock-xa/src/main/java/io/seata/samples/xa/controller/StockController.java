package io.seata.samples.xa.controller;

import io.seata.samples.xa.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {
    @Autowired
    private StockService stockService;

    @PostMapping("/xa/stock/deduct")
    public Boolean deduct(@RequestParam Long stockId, @RequestParam Long quantity) {
        return this.stockService.reduce(stockId, quantity);
    }
}
