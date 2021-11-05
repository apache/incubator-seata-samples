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
package io.seata.samples.tcc.transfer.activity;

/**
 * 转账服务
 *
 * @author zhangsen
 */
public interface TransferService {

    /**
     * 转账操作
     *
     * @param from   扣钱账户
     * @param to     加钱账户
     * @param amount 转账金额
     * @return
     */
    public boolean transfer(String from, String to, double amount);

}
