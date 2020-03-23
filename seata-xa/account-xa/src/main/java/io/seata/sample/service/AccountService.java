package io.seata.sample.service;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void reduce(String userId, int money) {
        String xid = RootContext.getXID();
        LOGGER.info("reduce account balance in transaction: " + xid);
        jdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", new Object[] {money, userId});
    }
}
