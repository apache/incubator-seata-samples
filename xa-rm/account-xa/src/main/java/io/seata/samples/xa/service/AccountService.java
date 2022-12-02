package io.seata.samples.xa.service;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean reduce(long accountId, long money) {
        String xid = RootContext.getXID();
        LOGGER.info("reduce account balance in transaction: " + xid);
        jdbcTemplate.update("update sys_account set balance = balance - ? where id = ?", new Object[] {money, accountId});
//        long balance = jdbcTemplate.queryForObject("select balance from sys_account where id = ?",
//            new Object[] {accountId}, Long.class);
//        LOGGER.info("balance after transaction: " + balance);
//        if (balance < 0) {
//            throw new RuntimeException("Not Enough Money ...");
//        }
        return true;
    }
}
