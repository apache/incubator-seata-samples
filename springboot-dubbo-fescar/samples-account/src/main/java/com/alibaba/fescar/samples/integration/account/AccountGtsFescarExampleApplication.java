package com.alibaba.fescar.samples.integration.account;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.alibaba.fescar.samples.integration.account")
@EnableDiscoveryClient
@MapperScan({"com.alibaba.fescar.samples.integration.account.mapper"})
@EnableDubbo(scanBasePackages = "com.alibaba.fescar.samples.integration.account")
public class AccountGtsFescarExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountGtsFescarExampleApplication.class, args);
    }

}

