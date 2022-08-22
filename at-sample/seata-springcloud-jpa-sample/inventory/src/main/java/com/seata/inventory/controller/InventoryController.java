package com.seata.inventory.controller;

import com.seata.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("inventory/deduct")
    public void deduct(@RequestParam("commodityCode") String commodityCode , @RequestParam("count") int count)  {
        inventoryService.deduct(commodityCode,count);
    }
}
