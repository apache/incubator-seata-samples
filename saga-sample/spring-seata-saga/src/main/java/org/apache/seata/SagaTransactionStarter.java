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

import io.seata.saga.engine.AsyncCallback;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.proctrl.ProcessContext;
import io.seata.saga.statelang.domain.ExecutionStatus;
import io.seata.saga.statelang.domain.StateMachineInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SagaTransactionStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaTransactionStarter.class);

    public static void main(String[] args) {
        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"spring/seata-saga.xml"});
        applicationContext.start();

        StateMachineEngine stateMachineEngine = (StateMachineEngine) applicationContext.getBean("stateMachineEngine");

        transactionCommittedDemo(stateMachineEngine);

        transactionCompensatedDemo(stateMachineEngine);
    }

    private static void transactionCommittedDemo(StateMachineEngine stateMachineEngine) {

        Map<String, Object> startParams = new HashMap<>(3);
        String businessKey = String.valueOf(System.currentTimeMillis());
        startParams.put("businessKey", businessKey);
        startParams.put("count", 10);
        startParams.put("amount", new BigDecimal("100"));

        //sync test
        StateMachineInstance inst = stateMachineEngine.startWithBusinessKey("reduceInventoryAndBalance", null,
                businessKey, startParams);

        Assert.isTrue(ExecutionStatus.SU.equals(inst.getStatus()),
                "saga transaction execute failed. XID: " + inst.getId());
        LOGGER.info("saga transaction commit succeed. XID: " + inst.getId());

        inst = stateMachineEngine.getStateMachineConfig().getStateLogStore().getStateMachineInstanceByBusinessKey(
                businessKey, null);
        Assert.isTrue(ExecutionStatus.SU.equals(inst.getStatus()),
                "saga transaction execute failed. XID: " + inst.getId());

        //async test
        businessKey = String.valueOf(System.currentTimeMillis());
        inst = stateMachineEngine.startWithBusinessKeyAsync("reduceInventoryAndBalance", null, businessKey, startParams,
                CALL_BACK);

        waitingForFinish(inst);

        Assert.isTrue(ExecutionStatus.SU.equals(inst.getStatus()),
                "saga transaction execute failed. XID: " + inst.getId());
        LOGGER.info("saga transaction commit succeed. XID: " + inst.getId());
    }

    private static void transactionCompensatedDemo(StateMachineEngine stateMachineEngine) {
        Map<String, Object> startParams = new HashMap<>(4);
        String businessKey = String.valueOf(System.currentTimeMillis());
        startParams.put("businessKey", businessKey);
        startParams.put("count", 10);
        startParams.put("amount", new BigDecimal("100"));
        startParams.put("mockReduceBalanceFail", "true");

        //async test
        businessKey = String.valueOf(System.currentTimeMillis());
        StateMachineInstance inst = stateMachineEngine.startWithBusinessKeyAsync("reduceInventoryAndBalance", null, businessKey, startParams,
                CALL_BACK);

        waitingForFinish(inst);

        Assert.isTrue(ExecutionStatus.SU.equals(inst.getCompensationStatus()),
                "saga transaction compensate failed. XID: " + inst.getId());
        LOGGER.info("saga transaction compensate succeed. XID: " + inst.getId());
    }

    private static final Object LOCK = new Object();

    private static final AsyncCallback CALL_BACK = new AsyncCallback() {
        @Override
        public void onFinished(ProcessContext context, StateMachineInstance stateMachineInstance) {
            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        }

        @Override
        public void onError(ProcessContext context, StateMachineInstance stateMachineInstance, Exception exp) {
            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        }
    };

    private static void waitingForFinish(StateMachineInstance inst) {
        synchronized (LOCK) {
            if (ExecutionStatus.RU.equals(inst.getStatus())) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.error("Thread was interrupted", e);
                }
            }
        }
    }
}
