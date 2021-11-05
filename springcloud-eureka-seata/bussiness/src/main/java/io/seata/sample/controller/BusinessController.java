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
package io.seata.sample.controller;

import io.seata.sample.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    /**
     * 购买下单，模拟全局事务提交
     *
     * @return
     */
    @RequestMapping(value = "/purchase/commit", produces = "application/json")
    public String purchaseCommit() {
        try {
            businessService.purchase("U100000", "C100000", 30);
        } catch (Exception exx) {
            return exx.getMessage();
        }
        return "全局事务提交";
    }

    /**
     * 购买下单，模拟全局事务回滚
     * 账户或库存不足
     *
     * @return
     */
    @RequestMapping("/purchase/rollback")
    public String purchaseRollback() {
        try {
            businessService.purchase("U100000", "C100000", 99999);
        } catch (Exception exx) {
            return exx.getMessage();
        }
        return "全局事务提交";
    }
}
