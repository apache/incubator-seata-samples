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
package org.apache.seata.consumer.service;

import org.apache.seata.provider.action.SagaActionOne;
import org.apache.seata.provider.action.SagaActionTwo;
import org.apache.seata.spring.annotation.GlobalTransactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Tcc transaction service.
 *
 * @author zhangsen
 */
public class SagaAnnotationTransactionService {

    private SagaActionOne sagaActionOne;

    private SagaActionTwo sagaActionTwo;

    /**
     * 发起分布式事务
     *
     * @return string string
     */
    @GlobalTransactional
    public void doTransactionCommit() {
        //第一个TCC 事务参与者
        boolean result = sagaActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");
        result = sagaActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
    }




    /**
     * Do transaction rollback string.
     */
    @GlobalTransactional
    public void doTransactionRollback() {
        //第一个TCC 事务参与者
        boolean result = sagaActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");
        result = sagaActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }

        throw new RuntimeException("transaction rollback");
    }

    public void setSagaActionOne(SagaActionOne sagaActionOne) {
        this.sagaActionOne = sagaActionOne;
    }

    public void setSagaActionTwo(SagaActionTwo sagaActionTwo) {
        this.sagaActionTwo = sagaActionTwo;
    }
}
