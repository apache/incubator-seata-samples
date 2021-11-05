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
package io.seata.samples.tcc.transfer.activity.impl;

import io.seata.samples.tcc.transfer.action.FirstTccAction;
import io.seata.samples.tcc.transfer.action.SecondTccAction;
import io.seata.samples.tcc.transfer.activity.TransferService;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * 转账服务实现
 *
 * @author zhangsen
 */
public class TransferServiceImpl implements TransferService {

    private FirstTccAction firstTccAction;

    private SecondTccAction secondTccAction;

    /**
     * 转账操作
     *
     * @param from   扣钱账户
     * @param to     加钱账户
     * @param amount 转账金额
     * @return
     */
    @Override
    @GlobalTransactional
    public boolean transfer(final String from, final String to, final double amount) {
        //扣钱参与者，一阶段执行
        boolean ret = firstTccAction.prepareMinus(null, from, amount);

        if (!ret) {
            //扣钱参与者，一阶段失败; 回滚本地事务和分布式事务
            throw new RuntimeException("账号:[" + from + "] 预扣款失败");
        }

        //加钱参与者，一阶段执行
        ret = secondTccAction.prepareAdd(null, to, amount);

        if (!ret) {
            throw new RuntimeException("账号:[" + to + "] 预收款失败");
        }

        System.out.println(
            String.format("transfer amount[%s] from [%s] to [%s] finish.", String.valueOf(amount), from, to));
        return true;
    }

    public void setFirstTccAction(FirstTccAction firstTccAction) {
        this.firstTccAction = firstTccAction;
    }

    public void setSecondTccAction(SecondTccAction secondTccAction) {
        this.secondTccAction = secondTccAction;
    }
}
