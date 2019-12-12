package io.seata.samples.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.seata.spring.annotation.GlobalTransactionScanner;

@Configuration
public class SeataAutoConfig {
    private final static Logger logger = LoggerFactory.getLogger(SeataAutoConfig.class);

    /**
     * init global transaction scanner
     *
     * @Return: GlobalTransactionScanner
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        logger.info("配置seata........");
        return new GlobalTransactionScanner("test-provider", "my_test_tx_group");
    }
}
