package com.alibaba.fescar.samples.integration.call;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.alibaba.fescar.samples.integration.call")
@EnableDiscoveryClient
@EnableDubbo(scanBasePackages = "com.alibaba.fescar.samples.integration.call")
public class DubboGtsFescarExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboGtsFescarExampleApplication.class, args);
    }

}

