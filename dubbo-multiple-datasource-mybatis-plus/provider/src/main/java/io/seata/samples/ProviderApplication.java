package io.seata.samples;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 
 * @author funkye
 * @date 2019/12/7
 */
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProviderApplication.class);
        app.run(args);
    }

}
