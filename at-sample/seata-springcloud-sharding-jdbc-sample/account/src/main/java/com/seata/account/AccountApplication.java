package com.seata.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.seata.account.mapper")
@EnableDiscoveryClient
@EnableFeignClients
public class AccountApplication {


    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }

}
