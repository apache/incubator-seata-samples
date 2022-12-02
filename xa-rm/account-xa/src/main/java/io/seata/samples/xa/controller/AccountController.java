package io.seata.samples.xa.controller;

import io.seata.samples.xa.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/xa/account/reduce")
    public Boolean create(@RequestParam Long accountId, @RequestParam Long orderMoney) {
        return this.accountService.reduce(accountId, orderMoney);
    }

    @GetMapping("/xa/account/hang")
    public Boolean hang(){
        System.out.println("hanging");
        return true;
    }
}
