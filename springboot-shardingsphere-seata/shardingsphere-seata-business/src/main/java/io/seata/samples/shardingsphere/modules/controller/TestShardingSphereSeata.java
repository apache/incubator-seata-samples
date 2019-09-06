package io.seata.samples.shardingsphere.modules.controller;

import io.seata.samples.shardingsphere.modules.service.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestShardingSphereSeata {

    @Autowired
    IBusinessService businessService;

    @RequestMapping(value = "test",method = RequestMethod.GET)
    @ResponseBody
    public void getCounsumerTest(){
        businessService.purchase();
    }


}
