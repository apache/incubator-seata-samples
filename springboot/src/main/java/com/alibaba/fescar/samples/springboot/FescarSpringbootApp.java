/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
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

package com.alibaba.fescar.samples.springboot;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.alibaba.fescar.samples.springboot.service.AssignService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The type Fescar springboot app.
 *
 * @author 张国豪
 * @Description
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDubboConfiguration
@EnableTransactionManagement
public class FescarSpringbootApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(FescarSpringbootApp.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		LOGGER.debug("springboot project with fescar starting...");
		ConfigurableApplicationContext context = new SpringApplication(FescarSpringbootApp.class).run(args);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOGGER.error("error when sleep", e);
		}
		AssignService assignService = context.getBean(AssignService.class);
		assignService.increaseAmount("14070e0e3cfe403098fa9ca37e8e7e76");
	}

}
