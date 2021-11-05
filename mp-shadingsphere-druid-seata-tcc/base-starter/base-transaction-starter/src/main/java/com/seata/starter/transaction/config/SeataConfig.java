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
package com.seata.starter.transaction.config;

import io.seata.spring.annotation.GlobalTransactionalInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class SeataConfig {
    private static final String AOP_POINTCUT_EXPRESSION = "@annotation(io.seata.spring.annotation.GlobalTransactional)";

    @Bean
    public GlobalTransactionalInterceptor globalTransactionalInterceptor() {
        GlobalTransactionalInterceptor globalTransactionalInterceptor = new GlobalTransactionalInterceptor(null);
        return globalTransactionalInterceptor;
    }

    @Bean
    public Advisor seataAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, globalTransactionalInterceptor());
    }
}
