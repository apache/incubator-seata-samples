package io.seata.samples.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "io.seata.samples", exclude = DataSourceAutoConfiguration.class)
public class SpringbootMybatisOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisOrderApplication.class, args);
    }

}
