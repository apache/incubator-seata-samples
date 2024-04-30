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
package org.apache.seata.controller;

import org.apache.seata.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class AccountController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private AccountService accountService;

    @RequestMapping(value = "/debit", method = RequestMethod.GET, produces = "application/json")
    public String debit(String userId, int money) {
        try {
            accountService.debit(userId, money);
        } catch (Exception ex) {
            LOGGER.error("debit err,", ex);
            throw ex;
        }
        return "SUCCESS";
    }
}
