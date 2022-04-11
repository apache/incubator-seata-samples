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
package com.demo.modules.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.modules.order.action.UserOrderTccAction;
import com.demo.modules.order.entity.UserOrder;
import com.demo.modules.order.mapper.UserOrderMapper;
import com.demo.modules.order.service.UserOderService;
import com.seata.common.api.vo.Result;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class UserOderServiceImpl extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOderService {
    @Autowired
    private UserOrderTccAction userOrderTccAction;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    @GlobalTransactional
    @Transactional
    public void geneOrder(UserOrder userOrder) {
        // 扣减库存
        ResponseEntity<Result> forEntity = restTemplate.getForEntity("http://localhost:8071/deduct?id=1", Result.class);
        if (forEntity.getStatusCode() != HttpStatus.OK || Optional.ofNullable(forEntity.getBody()).orElse(new Result())
            .getCode() != 200) {
            throw new RuntimeException("扣减库存失败！");
        }
        // 生成订单
        long id = IdWorker.getId();
        userOrder.setId(id);
        userOrderTccAction.geneOrder(userOrder, id);

        // 校验 事务回滚
        int a = 1 / 0;
    }
}
