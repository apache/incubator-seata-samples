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
package io.seata.samples.saga.action;

import io.seata.saga.annotation.api.SagaTransactional;
import io.seata.spring.annotation.LocalService;

/**
 * Inventory Actions
 */
@LocalService
public interface InventoryAction {

    /**
     * reduce
     *
     * @param count
     * @return
     */
    @SagaTransactional(name = "InventoryAction", compensationMethod = "compensateReduce", isDelayReport = true, useCommonFence = true)
    boolean reduce(String businessKey, int count);

    /**
     * increase
     *
     * @return
     */
    boolean compensateReduce(String businessKey);
}
