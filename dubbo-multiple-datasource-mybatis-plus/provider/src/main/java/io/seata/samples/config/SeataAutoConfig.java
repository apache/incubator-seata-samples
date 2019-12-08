package io.seata.samples.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.WebApplicationContext;

import com.baomidou.dynamic.datasource.DynamicDataSourceConfigure;
import com.baomidou.dynamic.datasource.DynamicDataSourceCreator;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAdvisor;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.processor.DsHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.druid.DruidDynamicDataSourceConfiguration;

import io.seata.spring.annotation.GlobalTransactionScanner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import(DruidDynamicDataSourceConfiguration.class)
public class SeataAutoConfig {
    @Autowired(required = true)
    private final static Logger logger = LoggerFactory.getLogger(SeataAutoConfig.class);
    @Autowired
    private DynamicDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceProvider dynamicDataSourceProvider(DynamicDataSourceCreator dynamicDataSourceCreator) {
        return new YmlDynamicDataSourceProvider(properties, dynamicDataSourceCreator);
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceCreator
        dynamicDataSourceCreator(@Autowired(required = false) WebApplicationContext webApplicationContext) {
        DynamicDataSourceCreator dynamicDataSourceCreator = new DynamicDataSourceCreator();
        dynamicDataSourceCreator.setApplicationContext(webApplicationContext);
        dynamicDataSourceCreator.setDruidGlobalConfig(properties.getDruid());
        dynamicDataSourceCreator.setHikariGlobalConfig(properties.getHikari());
        return dynamicDataSourceCreator;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        logger.info("加载数据源.......");
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setStrict(properties.getStrict());
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor();
        interceptor.setDsProcessor(dsProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor);
        advisor.setOrder(properties.getOrder());
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor() {
        DsHeaderProcessor headerProcessor = new DsHeaderProcessor();
        DsSessionProcessor sessionProcessor = new DsSessionProcessor();
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        headerProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(spelExpressionProcessor);
        return headerProcessor;
    }

    @Bean
    @ConditionalOnBean(DynamicDataSourceConfigure.class)
    public DynamicDataSourceAdvisor dynamicAdvisor(DynamicDataSourceConfigure dynamicDataSourceConfigure,
        DsProcessor dsProcessor) {
        DynamicDataSourceAdvisor advisor = new DynamicDataSourceAdvisor(dynamicDataSourceConfigure.getMatchers());
        advisor.setDsProcessor(dsProcessor);
        return advisor;
    }

    /**
     * init global transaction scanner
     *
     * @Return: GlobalTransactionScanner
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        logger.info("配置seata........");
        return new GlobalTransactionScanner("test-provider", "test-provider");
    }
}
