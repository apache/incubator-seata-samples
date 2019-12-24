package io.seata.starter.business.service;

import io.seata.starter.business.api.BusinessApi;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.starter.business.mapper.BusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

/**
 * @author lkx_soul
 * @create 2019-12-23-14:40
 * @Description  Business Service
 */
@RestController
public class BusinessService implements BusinessApi {

    @Autowired
    BusinessMapper businessMapper;

    @GetMapping("insertInfo")
    @GlobalTransactional
    public String insertInfo(Integer count, Double money,boolean isBack) {
        String sid = "soul"; //商品ID
        int i = businessMapper.insertInfo(sid, count, money);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject("http://localhost:8999/updateAccount/" + sid + "/" + count, String.class);
        if(isBack){
            throw new RuntimeException("执行回滚操作...");
        }
        return "二阶段事务提交";
    }
}
