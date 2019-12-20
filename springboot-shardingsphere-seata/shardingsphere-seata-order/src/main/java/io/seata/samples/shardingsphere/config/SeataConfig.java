package io.seata.samples.shardingsphere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeataConfig {

    @Bean
    public GlobalTransactionScanner globalTransactionScanner(){
        /**
         * 以下参数为是无效参数,具体可以查看该类源码
         */
        return new GlobalTransactionScanner("raw-jdbc-business","raw-jdbc-group");
    }
}
