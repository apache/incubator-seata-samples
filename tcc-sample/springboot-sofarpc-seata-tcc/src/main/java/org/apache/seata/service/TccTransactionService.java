/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.seata.service;

import com.alipay.sofa.runtime.spring.factory.ReferenceFactoryBean;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.apache.seata.action.ResultHolder;
import org.apache.seata.action.TccActionOne;
import org.apache.seata.action.TccActionTwo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class TccTransactionService implements InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;

    private TccActionOne tccActionOne;

    private TccActionTwo tccActionTwo;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * use ReferenceFactoryBean not sofaReference annotation. only ReferenceFactoryBean can weave tcc advice
     */
    @Configuration
    public static class SofaRpcReferenceConfig {

        @Bean
        public ReferenceFactoryBean tccActionOneService() {
            ReferenceFactoryBean referenceFactoryBean = new ReferenceFactoryBean();
            referenceFactoryBean.setInterfaceClass(TccActionOne.class);
            referenceFactoryBean.setBeanId("tccActionOneService");
            return referenceFactoryBean;
        }

        @Bean
        public ReferenceFactoryBean tccActionTwoService() {
            ReferenceFactoryBean referenceFactoryBean = new ReferenceFactoryBean();
            referenceFactoryBean.setInterfaceClass(TccActionTwo.class);
            referenceFactoryBean.setBeanId("tccActionTwoService");
            return referenceFactoryBean;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setTccActionOne((TccActionOne) beanFactory.getBean("tccActionOneService"));
        this.setTccActionTwo((TccActionTwo) beanFactory.getBean("tccActionTwoService"));
    }

    /**
     * 发起分布式事务
     *
     * @return string string
     */
    @GlobalTransactional
    public String doTransactionCommit() {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");
        result = tccActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        return RootContext.getXID();
    }

    public void checkBranchTransaction(String xid, boolean commit) {
        String actionOneResult = ResultHolder.getActionOneResult(xid);
        String actionTwoResult = ResultHolder.getActionTwoResult(xid);
        Assert.isTrue(commit ? "T".equals(actionOneResult) : "F".equals(actionOneResult), "分支事务" + (commit ? "提交" : "回滚") + "失败");
        Assert.isTrue(commit ? "T".equals(actionTwoResult) : "F".equals(actionTwoResult), "分支事务" + (commit ? "提交" : "回滚") + "失败");
    }

    /**
     * Do transaction rollback string.
     *
     * @return the string
     */
    @GlobalTransactional
    public void doTransactionRollback() {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");
        result = tccActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }

        throw new RuntimeException("transaction rollback");
    }

    /**
     * Sets tcc action one.
     *
     * @param tccActionOne the tcc action one
     */
    public void setTccActionOne(TccActionOne tccActionOne) {
        this.tccActionOne = tccActionOne;
    }

    /**
     * Sets tcc action two.
     *
     * @param tccActionTwo the tcc action two
     */
    public void setTccActionTwo(TccActionTwo tccActionTwo) {
        this.tccActionTwo = tccActionTwo;
    }
}
