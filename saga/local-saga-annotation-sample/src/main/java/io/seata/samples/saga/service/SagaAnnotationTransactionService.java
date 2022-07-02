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
package io.seata.samples.saga.service;

import io.seata.core.context.RootContext;
import io.seata.samples.saga.action.BalanceAction;
import io.seata.samples.saga.action.InventoryAction;
import io.seata.spring.annotation.GlobalTransactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Saga annotation transaction service.
 *
 * @author ruishansun
 */
public class SagaAnnotationTransactionService {


    private BalanceAction balanceAction;

    private InventoryAction inventoryAction;
    

    /**
     * 发起分布式事务
     *
     * @return string string
     */
    @GlobalTransactional
    public String doTransactionCommit() {


        String businessKey = String.valueOf(System.currentTimeMillis());
        
        boolean result = balanceAction.reduce(businessKey, new BigDecimal("100"), null);
        if (!result) {
            throw new RuntimeException("balanceAction failed.");
        }
        result = inventoryAction.reduce(businessKey, 10);
        if (!result) {
            throw new RuntimeException("inventoryAction failed.");
        }
        return RootContext.getXID();
    }

    /**
     * Do transaction rollback string.
     *
     * @param map the map
     * @return the string
     */
    @GlobalTransactional
    public String doTransactionRollback(Map map) {
        Map<String, Object> startParams = new HashMap<>(3);
        String businessKey = String.valueOf(System.currentTimeMillis());

        boolean result = balanceAction.reduce(businessKey, new BigDecimal("100"), null);
        if (!result) {
            throw new RuntimeException("balanceAction failed.");
        }
        result = inventoryAction.reduce(businessKey, 10);
        if (!result) {
            throw new RuntimeException("inventoryAction failed.");
        }
        map.put("xid", RootContext.getXID());
        throw new RuntimeException("transacton rollback");
    }

    /**
     * Sets tcc action one.
     *
     * @param balanceAction the tcc action one
     */
    public void setBalanceAction(BalanceAction balanceAction) {
        this.balanceAction = balanceAction;
    }

    /**
     * Sets tcc action two.
     *
     * @param inventoryAction the tcc action two
     */
    public void setInventoryAction(InventoryAction inventoryAction) {
        this.inventoryAction = inventoryAction;
    }
}
