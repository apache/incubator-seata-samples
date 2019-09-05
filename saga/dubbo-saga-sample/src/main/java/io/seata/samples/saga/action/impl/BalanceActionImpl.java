/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package io.seata.samples.saga.action.impl;

import io.seata.samples.saga.action.BalanceAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author lorne.cl
 */
public class BalanceActionImpl implements BalanceAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceActionImpl.class);

    @Override
    public boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params) {
        if(params != null && "true".equals(params.get("throwException"))){
            throw new RuntimeException("reduce balance failed");
        }
        LOGGER.info("reduce balance succeed, amount: " + amount + ", businessKey:" + businessKey);
        return true;
    }

    @Override
    public boolean compensateReduce(String businessKey, Map<String, Object> params) {
        if(params != null && "true".equals(params.get("throwException"))){
            throw new RuntimeException("compensate reduce balance failed");
        }
        LOGGER.info("compensate reduce balance succeed, businessKey:" + businessKey);
        return true;
    }
}