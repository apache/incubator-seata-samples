package io.seata.sample.controller;

import io.seata.sample.TestDatas;
import io.seata.sample.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static io.seata.sample.service.BusinessService.SUCCESS;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @RequestMapping(value = "/purchase", method = RequestMethod.GET, produces = "application/json")
    public String purchase(Boolean rollback, Integer count) {
        int orderCount = 30;
        if (count != null) {
            orderCount = count;
        }
        try {
            businessService.purchase(TestDatas.USER_ID, TestDatas.COMMODITY_CODE, orderCount,
                rollback == null ? false : rollback.booleanValue());
        } catch (Exception exx) {
            return "Purchase Failed:" + exx.getMessage();
        }
        return SUCCESS;
    }
}
