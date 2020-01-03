package io.seata.starter.samples.service;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.starter.samples.api.AccountApi;
import io.seata.starter.samples.mapper.AccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * @author lkx_soul
 * @create 2019-12-23-14:40
 * @Description  Seata Service
 */
@RestController
public class AccountService implements AccountApi {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountMapper accountMapper;

    @Override
    @GetMapping("updateAccount/{sid}/{count}")
    public boolean updateAccount(@PathVariable String sid, @PathVariable int count) {
        log.info("purchase begin ... xid: " + RootContext.getXID());
        int whenCount = accountMapper.selectCount(sid);
        if(count > whenCount){
            throw new RuntimeException("库存不足，抛出异常...");
        }
        int i = accountMapper.updateAccount(sid, count);
        return i>0;
    }

}
