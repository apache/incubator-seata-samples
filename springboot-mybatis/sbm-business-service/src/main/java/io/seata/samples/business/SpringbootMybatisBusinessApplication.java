package io.seata.samples.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "io.seata.samples", exclude = DataSourceAutoConfiguration.class)
public class SpringbootMybatisBusinessApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisBusinessApplication.class, args);
	}

}
