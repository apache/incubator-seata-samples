package com.seata.inventory.controller;

import com.seata.inventory.service.InventoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("inventory/occupy")
    public void occupy(@Param("commodityCode") String commodityCode , @Param("count") int count)  {
        inventoryService.occupy(commodityCode,count);
    }

    @GetMapping("inventory/rollBackInventory")
    public void rollBackInventory(@Param("commodityCode") String commodityCode , @Param("count") int count)  {
        inventoryService.rollBackInventory(commodityCode,count);
    }
}
