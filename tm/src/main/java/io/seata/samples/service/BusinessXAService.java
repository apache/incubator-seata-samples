package io.seata.samples.service;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import io.seata.core.context.RootContext;
import io.seata.samples.TestDatas;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service public class BusinessXAService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessXAService.class);

    @Autowired private JdbcTemplate jdbcTemplate;

    @GlobalTransactional
    public void purchase(long accountId, long stockId, long quantity) {
        String xid = RootContext.getXID();
        LOGGER.info("New Transaction Begins: " + xid);
        boolean stockResult = deductStock(stockId, quantity);
        if (!stockResult) {
            throw new RuntimeException("库存服务调用失败,事务回滚!");
        }
        boolean orderResult = createOrder(accountId, stockId, quantity);
        if (!orderResult) {
            throw new RuntimeException("订单服务调用失败,事务回滚!");
        }
    }

//    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from sys_account");
        jdbcTemplate.update("delete from sys_order");
        jdbcTemplate.update("delete from sys_stock");
        jdbcTemplate.update(
            "insert into sys_account(id,balance) values( " + TestDatas.USER_ID + "," + TestDatas.INIT_BALANCE + ")");
        jdbcTemplate.update(
            "insert into sys_stock(id,quantity,price) values(" + TestDatas.STOCK_ID + "," + TestDatas.STOCK_QUANTITY
                + "," + TestDatas.STOCK_PRICE + ")");
    }

    private boolean deductStock(long stockId, long quantity) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("stockId", stockId);
        params.put("quantity", quantity);
        return JSON.parseObject(HttpRequest.post("http://127.0.0.1:1003/xa/stock/deduct").form(params)
            .header(RootContext.KEY_XID, RootContext.getXID()).execute().body(), Boolean.class);
    }

    private boolean createOrder(long accountId, long stockId, long quantity) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("accountId", accountId);
        params.put("stockId", stockId);
        params.put("quantity", quantity);
        return JSON.parseObject(HttpRequest.post("http://127.0.0.1:1002/xa/order/create").form(params)
            .header(RootContext.KEY_XID, RootContext.getXID()).execute().body(), Boolean.class);
    }
}
