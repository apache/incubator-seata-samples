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

package org.apache.seata;

import org.apache.seata.e2e.E2EUtil;
import org.apache.seata.service.BusinessService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootSeataApplication implements BeanFactoryAware {

	private static BeanFactory BEAN_FACTORY;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringbootSeataApplication.class, args);

		BusinessService businessService = BEAN_FACTORY.getBean(BusinessService.class);

		Thread thread = new Thread(() -> {
			String res =  "{\"res\": \"success\"}";
			try {
				businessService.purchase("U100001", "C00321", 2);
				if (E2EUtil.isInE2ETest()) {
					E2EUtil.writeE2EResFile(res);
				}
			} catch (Exception e) {
				if (E2EUtil.isInE2ETest() && "random exception mock!".equals(e.getMessage())) {
					E2EUtil.writeE2EResFile(res);
				}
			}
		});
		thread.start();

		//keep run
		Thread.currentThread().join();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		BEAN_FACTORY = beanFactory;
	}
}
