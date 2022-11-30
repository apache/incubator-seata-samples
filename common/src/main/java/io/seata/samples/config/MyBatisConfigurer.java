package io.seata.samples.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {"io.seata.samples.mapper*"})
public class MyBatisConfigurer {
}
