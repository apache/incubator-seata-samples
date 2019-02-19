package com.alibaba.fescar.samples.integration.storage;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.alibaba.fescar.samples.integration.storage")
@EnableDiscoveryClient
@MapperScan({"com.alibaba.fescar.samples.integration.storage.mapper"})
@EnableDubbo(scanBasePackages = "com.alibaba.fescar.samples.integration.storage")
public class StorageGtsFescarExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageGtsFescarExampleApplication.class, args);
    }

}

