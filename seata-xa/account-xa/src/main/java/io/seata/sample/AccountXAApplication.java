package io.seata.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class AccountXAApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountXAApplication.class, args);
    }
}
