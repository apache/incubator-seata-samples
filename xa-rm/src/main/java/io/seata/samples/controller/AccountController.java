/*
 *  Copyright 1999-2022 Seata.io Group.
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
package io.seata.samples.controller;

import io.seata.samples.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping("/reduce")
    public Boolean reduce(@RequestParam Long accountId, @RequestParam BigDecimal money) {
        return this.accountService.reduce(accountId, money);
    }

    @PostMapping("/increase")
    public Boolean increase(@RequestParam Long accountId, @RequestParam BigDecimal money) {
        return this.accountService.increase(accountId, money);
    }

    @GetMapping("/getOne")
    public Map<String, Object> getOne(@RequestParam Long accountId) {
        return this.accountService.getOne(accountId);
    }

}
