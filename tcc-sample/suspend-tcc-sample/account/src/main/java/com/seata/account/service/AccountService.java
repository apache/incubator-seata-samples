package com.seata.account.service;



public interface AccountService {

    void occupy(String userId, int money);

    void rollBackAccount(String userId, int money);
}