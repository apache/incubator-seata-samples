package com.seata.starter.core.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
@ComponentScan(basePackages = {"com.yzs.starter.core"})
public class Knife4jConfiguration {

    @Value("${spring.swagger.description}")
    private String description;
    @Value("${spring.swagger.termsOfServiceUrl}")
    private String termsOfServiceUrl;
    @Value("${spring.swagger.version}")
    private String version;
    @Value("${spring.swagger.groupName}")
    private String groupName;
    @Value("${spring.swagger.basePackage}")
    private String basePackage;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //.title("swagger-bootstrap-ui-demo RESTful APIs")
                        .description(description)
                        .termsOfServiceUrl(termsOfServiceUrl)
                        .version(version)
                        .build())
                //分组名称
                .groupName(groupName)
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }
}