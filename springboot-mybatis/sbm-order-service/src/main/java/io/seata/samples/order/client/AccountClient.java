package io.seata.samples.order.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@Component
public class AccountClient {

    @Autowired
    private RestTemplate restTemplate;

    public void debit(String userId, BigDecimal orderMoney) {
        String url = "http://127.0.0.1:8083?userId=" + userId + "&orderMoney=" + orderMoney;
        try {
            restTemplate.getForEntity(url, Void.class);
        } catch (Exception e) {
            log.error("debit url {} ,error:", url, e);
            throw new RuntimeException();
        }
    }
}
