package io.seata.samples.saga.action.impl;

import io.seata.samples.saga.action.InventoryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lorne.cl
 */
public class InventoryActionImpl implements InventoryAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryActionImpl.class);

    @Override
    public boolean reduce(String businessKey, int count) {
        LOGGER.info("reduce inventory succeed, count: " + count + ", businessKey:" + businessKey);
        return true;
    }

    @Override
    public boolean compensateReduce(String businessKey) {
        LOGGER.info("compensate reduce inventory succeed, businessKey:" + businessKey);
        return true;
    }
}