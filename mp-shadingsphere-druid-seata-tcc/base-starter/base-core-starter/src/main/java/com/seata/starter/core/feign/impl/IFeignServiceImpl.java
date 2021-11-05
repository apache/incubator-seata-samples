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
package com.seata.starter.core.feign.impl;

import com.seata.common.constant.CommonConstant;
import com.seata.starter.core.feign.IFeignService;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
@Import(FeignClientsConfiguration.class)
public class IFeignServiceImpl implements IFeignService {

    //Feign 原生构造器
    Feign.Builder builder;

    //创建构造器
    public IFeignServiceImpl(Decoder decoder, Encoder encoder, Client client, Contract contract) {
        this.builder = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract);
    }

    @Override
    public <T> T newInstance(Class<T> clientClass, String serviceName) {
        builder.requestInterceptor(requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                log.info("Feign request: {}", request.getRequestURI());
                // 将token信息放入header中
                String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
                if (token == null) {
                    token = request.getParameter("token");
                }
                log.info("Feign request token: {}", token);
                requestTemplate.header(CommonConstant.X_ACCESS_TOKEN, token);
            }
        });
        return builder.target(clientClass, String.format("http://%s/", serviceName));
    }
}