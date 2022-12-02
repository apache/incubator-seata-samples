package io.seata.samples.xa.service;

import com.alibaba.fastjson.JSON;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import cn.hutool.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

@Service public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired private JdbcTemplate jdbcTemplate;

    public boolean createOrder(long accountId, long stockId, long quantity) {
        String xid = RootContext.getXID();
        LOGGER.info("create order in transaction: " + xid);

        // 定单总价 = 订购数量(count) * 商品单价(10)
        long orderMoney = quantity * 10;
        long orderId = 1000L;
        // 生成订单
        jdbcTemplate.update("insert into sys_order(id,account_id,stock_id,quantity,amount) values(?,?,?,?,?)",
            new Object[] {orderId, accountId, stockId, quantity, orderMoney});
        // 扣减余额
        boolean reduceAmountResult = reduceAmount(accountId, orderMoney);
        if (!reduceAmountResult) {
            throw new RuntimeException("Failed to call Account Service. ");
        }
        return reduceAmountResult;
    }

    private boolean reduceAmount(long accountId, long orderMoney) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("accountId", accountId);
        params.put("orderMoney", orderMoney);
        return JSON.parseObject(HttpRequest.post("http://127.0.0.1:1001/xa/account/reduce").form(params)
            .header(RootContext.KEY_XID, RootContext.getXID()).execute().body(), Boolean.class);
    }
}
