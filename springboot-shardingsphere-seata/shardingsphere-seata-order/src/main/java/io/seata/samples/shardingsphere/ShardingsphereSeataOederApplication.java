package io.seata.samples.shardingsphere;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

@MapperScan({
        "com.baomidou.springboot.mapper*" ,"io.seata.samples.shardingsphere.modules.mapper*"
})
@SpringBootApplication(exclude = {
        DruidDataSourceAutoConfigure.class,
        DataSourceAutoConfiguration.class,
        MybatisPlusAutoConfiguration.class,
        SpringBootConfiguration.class,
        RedisAutoConfiguration.class
})
@EnableDubbo(scanBasePackages = "io.seata.samples.shardingsphere.modules.service")
@EnableDiscoveryClient
public class ShardingsphereSeataOederApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingsphereSeataOederApplication.class, args);
    }

}
