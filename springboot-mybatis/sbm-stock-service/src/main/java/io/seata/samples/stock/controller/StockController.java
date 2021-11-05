package io.seata.samples.stock.controller;

import io.seata.samples.stock.persistence.Stock;
import io.seata.samples.stock.service.StockService;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RequestMapping("/api/stock")
@RestController
public class StockController {

    @Autowired
    StockService stockService;

    @GetMapping(value = "/deduct")
    public void deduct(@RequestParam String commodityCode, @RequestParam Integer count) throws SQLException {
        System.out.println("stock XID " + RootContext.getXID());
        stockService.deduct(commodityCode, count);
    }

    @GetMapping(value = "/get/{id}")
    public Stock getById(@PathVariable("id") Integer id) {
        return stockService.get(id);
    }

    @GetMapping(value = "/batch/update")
    public void batchUpdateCond() {
        try {
            stockService.batchUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/batch/delete")
    public void batchDeleteCond() {
        try {
            stockService.batchDelete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
