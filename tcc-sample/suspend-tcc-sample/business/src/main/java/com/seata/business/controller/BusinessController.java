package com.seata.business.controller;


import com.seata.business.service.BusinessService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("business/buy")
    @GlobalTransactional
    public String buy( String userId, String commodityCode, int count){
      return businessService.prepareBuy(userId,commodityCode,count);
    }

}
