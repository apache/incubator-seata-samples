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
package io.seata.samples.tcc.transfer.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * TCC参与者：加钱
 *
 * @author zhangsen
 */
public interface SecondTccAction {

    /**
     * 一阶段方法
     *
     * @param businessActionContext
     * @param accountNo
     * @param amount
     */
    @TwoPhaseBusinessAction(name = "secondTccAction", commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepareAdd(BusinessActionContext businessActionContext,
                              @BusinessActionContextParameter(paramName = "accountNo") String accountNo,
                              @BusinessActionContextParameter(paramName = "amount") double amount);

    /**
     * 二阶段提交
     *
     * @param businessActionContext
     * @return
     */
    public boolean commit(BusinessActionContext businessActionContext);

    /**
     * 二阶段回滚
     *
     * @param businessActionContext
     * @return
     */
    public boolean rollback(BusinessActionContext businessActionContext);

}
