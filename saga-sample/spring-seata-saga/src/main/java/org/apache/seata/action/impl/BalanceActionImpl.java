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
package org.apache.seata.action.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.seata.provider.action.BalanceAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lorne.cl
 */
public class BalanceActionImpl implements BalanceAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceActionImpl.class);

    @Override
    public boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params) {
        if(params != null) {
            Object throwException = params.get("throwException");
            if (throwException != null && "true".equals(throwException.toString())) {
                throw new RuntimeException("reduce balance failed");
            }
        }
        LOGGER.info("reduce balance succeed, amount: " + amount + ", businessKey:" + businessKey);
        return true;
    }

    @Override
    public boolean compensateReduce(String businessKey, Map<String, Object> params) {
        if(params != null) {
            Object throwException = params.get("throwException");
            if (throwException != null && "true".equals(throwException.toString())) {
                throw new RuntimeException("compensate reduce balance failed");
            }
        }
        LOGGER.info("compensate reduce balance succeed, businessKey:" + businessKey);
        return true;
    }
}