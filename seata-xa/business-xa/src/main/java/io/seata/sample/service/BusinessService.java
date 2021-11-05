package io.seata.sample.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import io.seata.core.context.RootContext;
import io.seata.sample.TestDatas;
import io.seata.sample.feign.OrderFeignClient;
import io.seata.sample.feign.StockFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    @Autowired
    private StockFeignClient stockFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GlobalTransactional
    public void purchase(String userId, String commodityCode, int orderCount, boolean rollback) {
        String xid = RootContext.getXID();
        LOGGER.info("New Transaction Begins: " + xid);

        String result = stockFeignClient.deduct(commodityCode, orderCount);

        if (!SUCCESS.equals(result)) {
            throw new RuntimeException("库存服务调用失败,事务回滚!");
        }

        result = orderFeignClient.create(userId, commodityCode, orderCount);

        if (!SUCCESS.equals(result)) {
            throw new RuntimeException("订单服务调用失败,事务回滚!");
        }

        if (rollback) {
            throw new RuntimeException("Force rollback ... ");
        }
    }

    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from account_tbl");
        jdbcTemplate.update("delete from order_tbl");
        jdbcTemplate.update("delete from stock_tbl");
        jdbcTemplate.update("insert into account_tbl(user_id,money) values('" + TestDatas.USER_ID + "','10000') ");
        jdbcTemplate.update(
            "insert into stock_tbl(commodity_code,count) values('" + TestDatas.COMMODITY_CODE + "','100') ");
    }

    public boolean validData(String userId, String commodityCode) {
        Map accountMap = jdbcTemplate.queryForMap("select * from account_tbl where user_id='" + userId + "'");
        if (Integer.parseInt(accountMap.get("money").toString()) < 0) {
            // 余额被扣减为负：余额不足
            return false;
        }

        Map stockMap = jdbcTemplate.queryForMap("select * from stock_tbl where commodity_code='" + commodityCode + "'");
        if (Integer.parseInt(stockMap.get("count").toString()) < 0) {
            // 库存被扣减为负：库存不足
            return false;
        }

        return true;
    }
}
