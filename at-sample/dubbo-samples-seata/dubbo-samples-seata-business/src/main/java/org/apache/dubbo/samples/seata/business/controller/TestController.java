/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.apache.dubbo.samples.seata.business.controller;

import com.alibaba.fastjson2.JSONObject;
import org.apache.dubbo.samples.seata.api.BusinessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private final BusinessService businessService;

    public TestController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/commit")
    public Object commit(@RequestParam(required = false, defaultValue = "ACC_001") String userId,
                         @RequestParam(required = false, defaultValue = "STOCK_001") String commodityCode,
                         @RequestParam(required = false, defaultValue = "1") int orderCount) {
        JSONObject jsonObject = new JSONObject();
        this.businessService.purchaseCommit(userId,commodityCode,orderCount);
        jsonObject.put("res", "commit");
        return jsonObject;
    }

    @GetMapping("/rollback")
    public Object rollback(@RequestParam(required = false, defaultValue = "ACC_001") String userId,
                           @RequestParam(required = false, defaultValue = "STOCK_001") String commodityCode,
                           @RequestParam(required = false, defaultValue = "1") int orderCount) {
        JSONObject jsonObject = new JSONObject();
        try {
            this.businessService.purchaseRollback(userId,commodityCode,orderCount);
            jsonObject.put("res", "rollback");
            return jsonObject;
        }catch (Exception e){
            jsonObject.put("res", "rollback");
            return jsonObject;
        }
    }
}
