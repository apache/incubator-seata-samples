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
package io.seata.samples.sca.provider.config;

import java.io.IOException;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import io.seata.rm.datasource.DataSourceProxy;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Description:
 * author: yu.hb
 * Date: 2019-11-01
 */
@Configuration
@EnableConfigurationProperties({MybatisProperties.class})
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean
    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSourceProxy dataSourceProxy,
                                                       MybatisProperties mybatisProperties) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSourceProxy);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] mapperLocaltions = resolver.getResources(mybatisProperties.getMapperLocations()[0]);
            bean.setMapperLocations(mapperLocaltions);

            if (StringUtils.isNotBlank(mybatisProperties.getConfigLocation())) {
                Resource[] resources = resolver.getResources(mybatisProperties.getConfigLocation());
                bean.setConfigLocation(resources[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
