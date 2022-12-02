package io.seata.samples.controller;

import io.seata.samples.TestDatas;
import io.seata.samples.service.BusinessXAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessXAController {
    @Autowired
    private BusinessXAService businessService;

    @RequestMapping(value = "/xa/purchase", method = RequestMethod.GET, produces = "application/json")
    public String purchase() {
        int orderCount = 1;
        try {
            businessService.purchase(TestDatas.USER_ID, TestDatas.STOCK_ID, orderCount);
        } catch (Exception exx) {
            return "Purchase Failed:" + exx.getMessage();
        }
        return "SUCCESS";
    }
}
