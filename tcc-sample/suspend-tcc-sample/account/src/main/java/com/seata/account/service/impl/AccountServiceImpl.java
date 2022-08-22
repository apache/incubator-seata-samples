package com.seata.account.service.impl;


import com.seata.account.mapper.AccountMapper;
import com.seata.account.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public void occupy(String userId, int money) {
        accountMapper.occupy(userId,money);
    }

    @Override
    public void rollBackAccount(String userId, int money) {
        accountMapper.rollBackAccount(userId,money);
    }
}
