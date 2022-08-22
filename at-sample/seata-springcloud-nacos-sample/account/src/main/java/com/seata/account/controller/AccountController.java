package com.seata.account.controller;


import com.seata.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("account/debit")
    public void debit(String userId, int money){

        accountService.debit(userId,money);
    };

}
