package io.seata.samples.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "io.seata.samples", exclude = DataSourceAutoConfiguration.class)
public class SpringbootMybatisStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisStockApplication.class, args);
    }

}
