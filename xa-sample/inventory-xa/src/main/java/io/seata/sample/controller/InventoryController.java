package io.seata.sample.controller;

import io.seata.sample.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static io.seata.sample.service.InventoryService.FAIL;
import static io.seata.sample.service.InventoryService.SUCCESS;

@RestController
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(value = "/deduct", method = RequestMethod.GET, produces = "application/json")
    public String deduct(String commodityCode, int count) {
        try {
            inventoryService.deduct(commodityCode, count);
        } catch (Exception exx) {
            exx.printStackTrace();
            return FAIL;
        }
        return SUCCESS;
    }
}
