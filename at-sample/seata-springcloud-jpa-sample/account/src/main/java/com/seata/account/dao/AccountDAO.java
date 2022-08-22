package com.seata.account.dao;


import com.seata.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountDAO extends JpaRepository<Account, Long> {

    Account findByUserId(String userId);

}
