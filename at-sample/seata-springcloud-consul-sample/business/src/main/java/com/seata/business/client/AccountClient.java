package com.seata.business.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "account-service")
@Component
public interface AccountClient {

    @GetMapping("account/debit")
    public void debit(@RequestParam("userId") String userId, @RequestParam("money")int money);

}