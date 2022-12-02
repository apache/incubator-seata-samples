package io.seata.samples.xa.service;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean reduce(Long stockId, Long quantity) {
        String xid = RootContext.getXID();
        LOGGER.info("deduct stock balance in transaction: " + xid);
        jdbcTemplate.update("update sys_stock set quantity = quantity - ? where id = ?",
            new Object[] {quantity, stockId});
//        long quantityNow = jdbcTemplate
//            .queryForObject("select quantity from sys_stock where id = ?", new Object[] {stockId}, Long.class);
//        LOGGER.info("quantity after transaction: " + quantityNow);
//        if (quantityNow < 0) {
//            throw new RuntimeException("Not Enough quantity ...");
//        }
        return true;
    }
}
