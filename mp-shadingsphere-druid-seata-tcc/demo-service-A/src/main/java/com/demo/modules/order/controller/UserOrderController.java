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
package com.demo.modules.order.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.demo.modules.order.action.UserOrderTccAction;
import com.demo.modules.order.entity.UserOrder;
import com.demo.modules.order.service.UserOderService;
import com.seata.common.api.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户订单")
public class UserOrderController {
    @Autowired
    private UserOderService userOderService;

    @GetMapping("/geneOrder")
    @ApiOperation("生成订单")
    public Result<?> geneOrder() {
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderId(IdWorker.getId());
        userOrder.setPId(1L);
        userOderService.geneOrder(userOrder);
        return Result.OK(userOrder);
    }
}
