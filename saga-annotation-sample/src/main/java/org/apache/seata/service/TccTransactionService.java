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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.seata.action.TccActionOne;
import org.apache.seata.action.TccActionTwo;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Component;

/**
 * The type Tcc transaction service.
 *
 * @author zhangsen
 */
@Component
public class TccTransactionService {

    @Resource
    private TccActionOne tccActionOne;

    @Resource
    private TccActionTwo tccActionTwo;

    /**
     * 发起分布式事务
     *
     * @return string string
     */
    @GlobalTransactional
    public String doTransactionCommit() {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.commit(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");
        result = tccActionTwo.commit(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        return RootContext.getXID();
    }

    /**
     * Do transaction rollback string.
     */
    @GlobalTransactional
    public void doTransactionRollback() {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.commit(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");
        result = tccActionTwo.commit(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        throw new RuntimeException("transaction rollback");
    }

}
