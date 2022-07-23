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

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.saga.annotation.api.SagaTransactional;
import io.seata.spring.annotation.LocalService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Balance Actions
 */
@LocalService
public interface BalanceAction {

    /**
     * reduce
     *
     * @param businessKey
     * @param amount
     * @param params
     * @return
     */
    @SagaTransactional(name = "BalanceAction" , compensationMethod = "compensateReduce", isDelayReport = true, useCommonFence = false)
    boolean reduce(BusinessActionContext actionContext, String businessKey, BigDecimal amount, Map<String, Object> params);

    /**
     * compensateReduce
     *
     * @param actionContext
     * @return
     */
    boolean compensateReduce(BusinessActionContext actionContext);

}
