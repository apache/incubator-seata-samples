package io.seata.samples.integration.stock;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.seata.samples.integration.stock")
@MapperScan({"io.seata.samples.integration.stock.mapper"})
@EnableDubbo(scanBasePackages = "io.seata.samples.integration.stock")
public class StockExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockExampleApplication.class, args);
    }

}

