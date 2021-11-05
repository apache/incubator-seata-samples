package io.seata.sample.service;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void reduce(String userId, int money) {
        String xid = RootContext.getXID();
        LOGGER.info("reduce account balance in transaction: " + xid);
        jdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", new Object[] {money, userId});
        int balance = jdbcTemplate.queryForObject("select money from account_tbl where user_id = ?",
            new Object[] {userId}, Integer.class);
        LOGGER.info("balance after transaction: " + balance);
        if (balance < 0) {
            throw new RuntimeException("Not Enough Money ...");
        }
    }
}
