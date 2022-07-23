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
package io.seata.samples.saga.action.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.samples.saga.action.BalanceAction;
import io.seata.samples.saga.action.ResultHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author lorne.cl
 */
public class BalanceActionImpl implements BalanceAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceActionImpl.class);

    @Override
    public boolean reduce(BusinessActionContext actionContext, String businessKey, BigDecimal amount, Map<String, Object> params) {
        if(params != null) {
            Object throwException = params.get("throwException");
            if (throwException != null && "true".equals(throwException.toString())) {
                throw new RuntimeException("reduce balance failed");
            }
        }
        LOGGER.info("reduce balance succeed, amount: " + amount + ", businessKey:" + businessKey);
        ResultHolder.setActionOneResult(actionContext.getXid(), "T");
        return true;
    }

    @Override
    public boolean compensateReduce(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("Balance compensate Reduce, xid:" + xid);
        ResultHolder.setActionOneResult(xid, "R");
        return true;
    }
}