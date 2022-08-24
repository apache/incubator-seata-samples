package io.seata.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderXAApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderXAApplication.class, args);
    }

}
