package io.seata.sample;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import io.seata.rm.datasource.xa.DataSourceProxyXA;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AccountXADataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Bean("dataSourceProxy")
    public DataSource dataSource(DruidDataSource druidDataSource) {
        // DataSourceProxy for AT mode
        // return new DataSourceProxy(druidDataSource);

        // DataSourceProxyXA for XA mode
        return new DataSourceProxyXA(druidDataSource);
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSourceProxy) {
        return new JdbcTemplate(dataSourceProxy);
    }

    @Bean
    public PlatformTransactionManager txManager(DataSource dataSourceProxy) {
        return new DataSourceTransactionManager(dataSourceProxy);
    }

}
