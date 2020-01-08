package io.seata.samples.integration.order;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.seata.samples.integration.order")
@MapperScan({"io.seata.samples.integration.order.mapper"})
@EnableDubbo(scanBasePackages = "io.seata.samples.integration.order")
public class OrderExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderExampleApplication.class, args);
    }

}

