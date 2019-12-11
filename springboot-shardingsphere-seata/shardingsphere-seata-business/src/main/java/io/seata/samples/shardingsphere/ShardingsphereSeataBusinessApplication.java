package io.seata.samples.shardingsphere;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan({
        "com.baomidou.springboot.mapper*" ,"io.seata.samples.shardingsphere.modules.mapper*"
})
@SpringBootApplication(exclude = {
        DruidDataSourceAutoConfigure.class,
        DataSourceAutoConfiguration.class,
        MybatisPlusAutoConfiguration.class,
        RedisAutoConfiguration.class
})
@EnableDubbo(scanBasePackages = "io.seata.samples.shardingsphere.modules.service")
@EnableDiscoveryClient
public class ShardingsphereSeataBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingsphereSeataBusinessApplication.class, args);
    }

}
