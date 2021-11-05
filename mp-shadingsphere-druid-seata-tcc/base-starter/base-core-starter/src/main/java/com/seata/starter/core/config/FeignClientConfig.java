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
package com.seata.starter.core.config;

import com.seata.common.constant.CommonConstant;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
@Slf4j
@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                log.info("Feign request: {}", request.getRequestURI());
                // 将token信息放入header中
                String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
                String tenantId = request.getHeader(CommonConstant.TENANT_ID);
                String uid = request.getHeader(CommonConstant.USER_ID);
                String sys_id = request.getHeader(CommonConstant.SYS_ID);
                String term_id = request.getHeader(CommonConstant.TERM_ID);
                String username = request.getHeader(CommonConstant.USERNAME);
                if (token == null) {
                    token = request.getParameter("token");
                }
                log.info("Feign request token: {}", token);
                requestTemplate.header(CommonConstant.TENANT_ID, request.getHeader(CommonConstant.TENANT_ID));
                requestTemplate.header(CommonConstant.USER_ID, request.getHeader(CommonConstant.USER_ID));
                requestTemplate.header(CommonConstant.USERNAME, request.getHeader(CommonConstant.USERNAME));
                requestTemplate.header(CommonConstant.SYS_ID, request.getHeader(CommonConstant.SYS_ID));
                requestTemplate.header(CommonConstant.TERM_ID, request.getHeader(CommonConstant.TERM_ID));
                requestTemplate.header(CommonConstant.X_ACCESS_TOKEN, token);
                requestTemplate.header(CommonConstant.TENANT_ID, tenantId);
                requestTemplate.header(CommonConstant.USER_ID, uid);
                requestTemplate.header(CommonConstant.SYS_ID, sys_id);
                requestTemplate.header(CommonConstant.TERM_ID, term_id);
                requestTemplate.header(CommonConstant.USERNAME, username);

            }
        };
    }

    /**
     * Feign 客户端的日志记录，默认级别为NONE
     * Logger.Level 的具体级别如下：
     * NONE：不记录任何信息
     * BASIC：仅记录请求方法、URL以及响应状态码和执行时间
     * HEADERS：除了记录 BASIC级别的信息外，还会记录请求和响应的头信息
     * FULL：记录所有请求与响应的明细，包括头信息、请求体、元数据
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Feign支持文件上传
     *
     * @param messageConverters
     * @return
     */
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
}
