package io.seata.samples.mutiple.datasource.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import io.seata.rm.datasource.DataSourceProxy;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author HelloWoodes
 */
@Configuration
public class DataSourceProxyConfig {

    @Bean("originOrder")
    @ConfigurationProperties(prefix = "spring.datasource.order")
    public DataSource dataSourceMaster() {
        return new DruidDataSource();
    }

    @Bean("originStock")
    @ConfigurationProperties(prefix = "spring.datasource.stock")
    public DataSource dataSourceStock() {
        return new DruidDataSource();
    }

    @Bean("originPay")
    @ConfigurationProperties(prefix = "spring.datasource.pay")
    public DataSource dataSourcePay() {
        return new DruidDataSource();
    }

    @Bean(name = "order")
    public DataSourceProxy masterDataSourceProxy(@Qualifier("originOrder") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "stock")
    public DataSourceProxy stockDataSourceProxy(@Qualifier("originStock") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "pay")
    public DataSourceProxy payDataSourceProxy(@Qualifier("originPay") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Primary
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("order") DataSource dataSourceOrder,
                                        @Qualifier("stock") DataSource dataSourceStock,
                                        @Qualifier("pay") DataSource dataSourcePay) {

        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>(3);
        dataSourceMap.put(DataSourceKey.ORDER.name(), dataSourceOrder);
        dataSourceMap.put(DataSourceKey.STOCK.name(), dataSourceStock);
        dataSourceMap.put(DataSourceKey.PAY.name(), dataSourcePay);

        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSourceOrder);
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

        DynamicDataSourceContextHolder.getDataSourceKeys().addAll(dataSourceMap.keySet());

        return dynamicRoutingDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dynamicDataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

}