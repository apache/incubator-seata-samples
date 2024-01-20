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
package org.apache.seata.provider;

import org.apache.curator.test.TestingServer;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

/**
 * @author wangte
 * Create At 2024/1/20
 */
@EnableDubbo(scanBasePackages = {"org.apache.seata.provider"})
@ComponentScan(basePackages = {"org.apache.seata.provider"})
public class DubboProviderStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboProviderStarter.class);

    private static TestingServer server;

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));

        mockZKServer();

        new AnnotationConfigApplicationContext(DubboProviderStarter.class);

        LOGGER.info("dubbo provider started");
    }

    private static void mockZKServer() throws Exception {
        server = new TestingServer(2181, true);
        server.start();
    }
}
