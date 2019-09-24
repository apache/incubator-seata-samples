/*
 *  Copyright 1999-2019 Seata.io Group.
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

import io.seata.samples.saga.action.InventoryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
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