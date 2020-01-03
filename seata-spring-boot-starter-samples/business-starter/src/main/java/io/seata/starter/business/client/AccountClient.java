package io.seata.starter.business.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
  *@author lkx_soul
  *@desc   Account client
 * @Vesion 1.0
 * @create 2019.12.25
  **/
@Component
public class AccountClient {

    private static final Logger log = LoggerFactory.getLogger(AccountClient.class);

    @Autowired
    private RestTemplate restTemplate;

    public void update(String sid, int count) {
        String url = "http://localhost:8999/updateAccount/" + sid + "/" + count;
        try {
            restTemplate.getForObject("http://localhost:8999/updateAccount/" + sid + "/" + count, String.class);
        } catch (Exception e) {
            log.error("create url {} ,error:", url);
            throw new RuntimeException();
        }
    }
}
