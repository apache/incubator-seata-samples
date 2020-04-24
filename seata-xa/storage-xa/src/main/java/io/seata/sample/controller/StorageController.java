package io.seata.sample.controller;

import io.seata.sample.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static io.seata.sample.service.StorageService.FAIL;
import static io.seata.sample.service.StorageService.SUCCESS;

@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/deduct", method = RequestMethod.GET, produces = "application/json")
    public String deduct(String commodityCode, int count) {
        try {
            storageService.deduct(commodityCode, count);
        } catch (Exception exx) {
            exx.printStackTrace();
            return FAIL;
        }
        return SUCCESS;
    }
}
