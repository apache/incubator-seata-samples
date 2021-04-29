package com.demo;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {
        MybatisPlusAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
})
@MapperScan(basePackages = {"com.demo.modules.**.mapper*"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ServiceAApp {
    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }
    public static void main(String[] args) {
        SpringApplication.run(ServiceAApp.class, args);
    }
}
