package io.seata.starter.business.service;

import io.seata.core.context.RootContext;
import io.seata.starter.business.api.BusinessApi;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.starter.business.client.AccountClient;
import io.seata.starter.business.mapper.BusinessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * @author lkx_soul
 * @create 2019-12-23-14:40
 * @Description  Business Service
 */
@RestController
public class BusinessService implements BusinessApi {

    private static final Logger log = LoggerFactory.getLogger(BusinessService.class);

    @Autowired
    BusinessMapper businessMapper;

    @Autowired
    AccountClient accountClient;

    @Override
    @GetMapping("insertInfo")
    @GlobalTransactional
    public String insertInfo(Integer count, Double money,boolean isBack) {
        log.info("--------------------purchase begin ... xid: " + RootContext.getXID());
        //商品ID
        String sid = "soul";
        int i = businessMapper.insertInfo(sid, count, money);
        RestTemplate restTemplate = new RestTemplate();
        accountClient.update(sid,count);
        if(isBack){
            throw new RuntimeException("执行回滚操作...");
        }
        return "二阶段事务提交";
    }
}
