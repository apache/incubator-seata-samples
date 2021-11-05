package io.seata.sample.service;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deduct(String commodityCode, int count) {
        String xid = RootContext.getXID();
        LOGGER.info("deduct stock balance in transaction: " + xid);
        jdbcTemplate.update("update stock_tbl set count = count - ? where commodity_code = ?",
            new Object[] {count, commodityCode});
    }
}
