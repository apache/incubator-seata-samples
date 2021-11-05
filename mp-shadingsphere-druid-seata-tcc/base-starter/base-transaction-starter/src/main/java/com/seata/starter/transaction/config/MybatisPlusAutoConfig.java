/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.seata.starter.transaction.config;

import com.baomidou.mybatisplus.autoconfigure.*;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.seata.starter.jdbc.config.MybatisPlusConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties({MybatisPlusProperties.class})
@AutoConfigureAfter(
    {DataSourceAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class, MybatisPlusConfig.class})
public class MybatisPlusAutoConfig implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MybatisPlusAutoConfig.class);
    private final MybatisPlusProperties properties;
    private final Interceptor[] interceptors;
    private final TypeHandler[] typeHandlers;
    private final LanguageDriver[] languageDrivers;
    private final ResourceLoader resourceLoader;
    private final DatabaseIdProvider databaseIdProvider;
    private final List<ConfigurationCustomizer> configurationCustomizers;
    private final List<MybatisPlusPropertiesCustomizer> mybatisPlusPropertiesCustomizers;
    private final ApplicationContext applicationContext;

    public MybatisPlusAutoConfig(MybatisPlusProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider,
                                 ObjectProvider<TypeHandler[]> typeHandlersProvider,
                                 ObjectProvider<LanguageDriver[]> languageDriversProvider,
                                 ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                 ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
                                 ObjectProvider<List<MybatisPlusPropertiesCustomizer>> mybatisPlusPropertiesCustomizerProvider,
                                 ApplicationContext applicationContext) {
        this.properties = properties;
        this.interceptors = (Interceptor[])interceptorsProvider.getIfAvailable();
        this.typeHandlers = (TypeHandler[])typeHandlersProvider.getIfAvailable();
        this.languageDrivers = (LanguageDriver[])languageDriversProvider.getIfAvailable();
        this.resourceLoader = resourceLoader;
        this.databaseIdProvider = (DatabaseIdProvider)databaseIdProvider.getIfAvailable();
        this.configurationCustomizers = (List)configurationCustomizersProvider.getIfAvailable();
        this.mybatisPlusPropertiesCustomizers = (List)mybatisPlusPropertiesCustomizerProvider.getIfAvailable();
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(this.mybatisPlusPropertiesCustomizers)) {
            this.mybatisPlusPropertiesCustomizers.forEach((i) -> {
                i.customize(this.properties);
            });
        }

        this.checkConfigFileExists();
    }

    private void checkConfigFileExists() {
        if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
            Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
            Assert.state(resource.exists(), "Cannot find config location: " + resource
                + " (please add config file or check your Mybatis configuration)");
        }

    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }

        this.applyConfiguration(factory);
        if (this.properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(this.properties.getConfigurationProperties());
        }

        if (!ObjectUtils.isEmpty(this.interceptors)) {
            factory.setPlugins(this.interceptors);
        }

        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }

        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }

        if (this.properties.getTypeAliasesSuperType() != null) {
            factory.setTypeAliasesSuperType(this.properties.getTypeAliasesSuperType());
        }

        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }

        if (!ObjectUtils.isEmpty(this.typeHandlers)) {
            factory.setTypeHandlers(this.typeHandlers);
        }

        Resource[] mapperLocations = this.properties.resolveMapperLocations();
        if (!ObjectUtils.isEmpty(mapperLocations)) {
            factory.setMapperLocations(mapperLocations);
        }

        this.getBeanThen(TransactionFactory.class, factory::setTransactionFactory);
        Class<? extends LanguageDriver> defaultLanguageDriver = this.properties.getDefaultScriptingLanguageDriver();
        if (!ObjectUtils.isEmpty(this.languageDrivers)) {
            factory.setScriptingLanguageDrivers(this.languageDrivers);
        }

        Optional.ofNullable(defaultLanguageDriver).ifPresent(factory::setDefaultScriptingLanguageDriver);
        if (StringUtils.hasLength(this.properties.getTypeEnumsPackage())) {
            factory.setTypeEnumsPackage(this.properties.getTypeEnumsPackage());
        }

        GlobalConfig globalConfig = this.properties.getGlobalConfig();
        this.getBeanThen(MetaObjectHandler.class, globalConfig::setMetaObjectHandler);
        this.getBeanThen(IKeyGenerator.class, (i) -> {
            globalConfig.getDbConfig().setKeyGenerator(i);
        });
        this.getBeanThen(ISqlInjector.class, globalConfig::setSqlInjector);
        this.getBeanThen(IdentifierGenerator.class, globalConfig::setIdentifierGenerator);
        factory.setGlobalConfig(globalConfig);
        return factory.getObject();
    }

    private <T> void getBeanThen(Class<T> clazz, Consumer<T> consumer) {
        if (this.applicationContext.getBeanNamesForType(clazz, false, false).length > 0) {
            consumer.accept(this.applicationContext.getBean(clazz));
        }

    }

    private void applyConfiguration(MybatisSqlSessionFactoryBean factory) {
        MybatisConfiguration configuration = this.properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
            configuration = new MybatisConfiguration();
        }

        if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
            Iterator var3 = this.configurationCustomizers.iterator();

            while (var3.hasNext()) {
                ConfigurationCustomizer customizer = (ConfigurationCustomizer)var3.next();
                customizer.customize(configuration);
            }
        }

        factory.setConfiguration(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        ExecutorType executorType = this.properties.getExecutorType();
        return executorType != null ? new SqlSessionTemplate(sqlSessionFactory, executorType) : new SqlSessionTemplate(
            sqlSessionFactory);
    }

    @Configuration
    @Import({MybatisPlusAutoConfig.AutoConfiguredMapperScannerRegistrar.class})
    @ConditionalOnMissingBean({MapperFactoryBean.class, MapperScannerConfigurer.class})
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {
        public MapperScannerRegistrarNotFoundConfiguration() {
        }

        public void afterPropertiesSet() {
            MybatisPlusAutoConfig.logger.debug(
                "Not found configuration for registering mapper bean using @MapperScan, MapperFactoryBean and "
                    + "MapperScannerConfigurer.");
        }
    }

    public static class AutoConfiguredMapperScannerRegistrar
        implements BeanFactoryAware, ImportBeanDefinitionRegistrar {
        private BeanFactory beanFactory;

        public AutoConfiguredMapperScannerRegistrar() {
        }

        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                            BeanDefinitionRegistry registry) {
            if (!AutoConfigurationPackages.has(this.beanFactory)) {
                MybatisPlusAutoConfig.logger.debug(
                    "Could not determine auto-configuration package, automatic mapper scanning disabled.");
            } else {
                MybatisPlusAutoConfig.logger.debug("Searching for mappers annotated with @Mapper");
                List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
                if (MybatisPlusAutoConfig.logger.isDebugEnabled()) {
                    packages.forEach((pkg) -> {
                        MybatisPlusAutoConfig.logger.debug("Using auto-configuration base package '{}'", pkg);
                    });
                }

                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
                    MapperScannerConfigurer.class);
                builder.addPropertyValue("processPropertyPlaceHolders", true);
                builder.addPropertyValue("annotationClass", Mapper.class);
                builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
                BeanWrapper beanWrapper = new BeanWrapperImpl(MapperScannerConfigurer.class);
                Stream.of(beanWrapper.getPropertyDescriptors()).filter((x) -> {
                    return x.getName().equals("lazyInitialization");
                }).findAny().ifPresent((x) -> {
                    builder.addPropertyValue("lazyInitialization", "${mybatis.lazy-initialization:false}");
                });
                registry.registerBeanDefinition(MapperScannerConfigurer.class.getName(), builder.getBeanDefinition());
            }
        }

        public void setBeanFactory(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }
    }
}
