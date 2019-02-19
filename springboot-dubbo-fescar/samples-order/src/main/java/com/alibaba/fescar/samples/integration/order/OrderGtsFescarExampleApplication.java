package com.alibaba.fescar.samples.integration.order;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.alibaba.fescar.samples.integration.order")
@EnableDiscoveryClient
@MapperScan({"com.alibaba.fescar.samples.integration.order.mapper"})
@EnableDubbo(scanBasePackages = "com.alibaba.fescar.samples.integration.order")
public class OrderGtsFescarExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderGtsFescarExampleApplication.class, args);
    }

}

