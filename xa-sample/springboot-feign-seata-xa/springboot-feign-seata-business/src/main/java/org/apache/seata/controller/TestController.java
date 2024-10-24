package org.apache.seata.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.seata.service.BusinessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private final BusinessService businessService;

    public TestController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/commit")
    public Object commit(@RequestParam(required = false, defaultValue = TestData.USER_ID) String userId,
                         @RequestParam(required = false, defaultValue = TestData.COMMODITY_CODE) String commodityCode,
                         @RequestParam(required = false, defaultValue = "2") int orderCount) {
        JSONObject jsonObject = new JSONObject();
        this.businessService.purchaseCommit(userId, commodityCode, orderCount);
        jsonObject.put("res", "commit");
        return jsonObject;
    }

    @GetMapping("/rollback")
    public Object rollback(@RequestParam(required = false, defaultValue =  TestData.USER_ID) String userId,
                           @RequestParam(required = false, defaultValue = TestData.COMMODITY_CODE) String commodityCode,
                           @RequestParam(required = false, defaultValue = "2") int orderCount) {
        JSONObject jsonObject = new JSONObject();
        try {
            this.businessService.purchaseRollback(userId, commodityCode, orderCount);
            jsonObject.put("res", "rollback");
            return jsonObject;
        }catch (Exception e){
            jsonObject.put("res", "rollback");
            return jsonObject;
        }
    }
}
