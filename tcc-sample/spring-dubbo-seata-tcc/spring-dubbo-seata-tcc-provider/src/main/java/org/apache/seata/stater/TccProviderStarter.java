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
package org.apache.seata.stater;

import org.apache.curator.test.TestingServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.apache.seata.e2e.E2EUtil.isInE2ETest;

public class TccProviderStarter {

    private static TestingServer server;

    public static void main(String[] args) throws Exception {
        new TccProviderStarter().start0(args);
    }

    protected void start0(String[] args) throws Exception {
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            if (server != null) {
//                try {
//                    server.close();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }));
//
//
//        //mock zk server
//        mockZKServer();

        if (isInE2ETest()) {
            // wait seata-server
            Thread.sleep(2000);
        }
        new ClassPathXmlApplicationContext( "spring/seata-dubbo-provider.xml");
        //keep run
        Thread.currentThread().join();
    }

    private static void mockZKServer() throws Exception {
        //Mock zk server，作为 transfer 配置中心
        server = new TestingServer(2181, true);
        server.start();
    }
}
