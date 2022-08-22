package com.seata.business.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "account-service")
@Component
public interface AccountClient {

    @GetMapping("account/occupy")
    public void occupy(@RequestParam("userId") String userId, @RequestParam("money")int money);

    @GetMapping("account/rollBackAccount")
    public void rollBackAccount(@RequestParam("userId") String userId, @RequestParam("money")int money);

}