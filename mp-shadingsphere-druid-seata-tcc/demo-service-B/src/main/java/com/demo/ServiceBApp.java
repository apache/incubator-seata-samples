package com.demo;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = {
        MybatisPlusAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
})
@MapperScan(basePackages = {"com.demo.modules.**.mapper*"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ServiceBApp {
    public static void main(String[] args) {
        SpringApplication.run(ServiceBApp.class, args);
    }
}
