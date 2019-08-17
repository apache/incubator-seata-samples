package io.seata.samples.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "io.seata.samples", exclude = DataSourceAutoConfiguration.class)
public class SpringbootMybatisStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisStorageApplication.class, args);
    }

}
