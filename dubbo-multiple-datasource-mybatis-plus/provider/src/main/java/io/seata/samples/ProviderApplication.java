package io.seata.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;

/**
 * 
 * @author funkye
 * @date 2019/12/7
 */
@SpringBootApplication(exclude = {DynamicDataSourceAutoConfiguration.class})
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProviderApplication.class);
        app.run(args);
    }

}
