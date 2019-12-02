package org.test;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;


/**
 * 
 * @author cjb
 * @date 2019/10/24
 */
@EnableTransactionManagement
@ComponentScan(basePackages = {"org.test.config","org.test.service.impl"})
@DubboComponentScan(basePackages = "org.test.service.impl")
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProviderApplication.class);
        app.run(args);
    }

}
