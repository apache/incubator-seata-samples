package com.seata.account.service.impl;



import com.seata.account.dao.AccountDAO;
import com.seata.account.model.Account;
import com.seata.account.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDAO accountDAO;

    public void debit(String userId, int money) {
        Account account = accountDAO.findByUserId(userId);
        account.setMoney(account.getMoney() - money);
        accountDAO.save(account);
    }
}
