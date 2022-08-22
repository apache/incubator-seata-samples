package com.seata.account.service;



public interface AccountService {

    void debit(String userId, int money);
}