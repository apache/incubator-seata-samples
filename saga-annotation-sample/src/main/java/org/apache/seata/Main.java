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

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.seata.action.ResultHolder;
import org.apache.seata.core.exception.TransactionException;
import org.apache.seata.service.SagaTransactionService;
import org.apache.seata.tm.api.GlobalTransaction;
import org.apache.seata.tm.api.GlobalTransactionContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = { "org.apache.seata"})
public class Main {
    public static void main(String[] args)
            throws IOException, ExecutionException, InterruptedException, TimeoutException, TransactionException {
        boolean isInE2ETest = isInE2ETest();
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        SagaTransactionService sagaTransactionService = context.getBean(SagaTransactionService.class);
        String xid = sagaTransactionService.doTransactionCommit();
        ResultHolder.getActionTwoResult(xid).get(10, TimeUnit.SECONDS);
        GlobalTransaction globalTransaction = GlobalTransactionContext.createNew();
        globalTransaction.begin();
        xid = globalTransaction.getXid();
        try {
            sagaTransactionService.doTransactionRollback();
        } catch (Exception e) {
            globalTransaction.rollback();
        }
        ResultHolder.getActionTwoResult(xid).get(10, TimeUnit.SECONDS);
    }

    public static boolean isInE2ETest() {
        Map<String, String> envs = System.getenv();
        String env = envs.getOrDefault("E2E_ENV", "");
        return "open".equals(env);
    }

}