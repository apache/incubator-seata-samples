package io.seata.samples.mutiple.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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

    @Bean("originStorage")
    @ConfigurationProperties(prefix = "spring.datasource.storage")
    public DataSource dataSourceStorage() {
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

    @Bean(name = "storage")
    public DataSourceProxy storageDataSourceProxy(@Qualifier("originStorage") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "pay")
    public DataSourceProxy payDataSourceProxy(@Qualifier("originPay") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("order") DataSource dataSourceOrder,
                                        @Qualifier("storage") DataSource dataSourceStorage,
                                        @Qualifier("pay") DataSource dataSourcePay) {

        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>(3);
        dataSourceMap.put(DataSourceKey.ORDER.name(), dataSourceOrder);
        dataSourceMap.put(DataSourceKey.STORAGE.name(), dataSourceStorage);
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