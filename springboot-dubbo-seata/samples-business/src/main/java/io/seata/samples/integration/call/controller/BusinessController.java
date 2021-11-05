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
package io.seata.samples.integration.call.controller;

import io.seata.samples.integration.call.service.BusinessService;
import io.seata.samples.integration.common.dto.BusinessDTO;
import io.seata.samples.integration.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: heshouyou
 * @Description Dubbo业务执行入口
 * @Date Created in 2019/1/14 17:15
 */
@RestController
@RequestMapping("/business/dubbo")
@Slf4j
public class BusinessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessController.class);

    @Autowired
    private BusinessService businessService;

    /**
     * 模拟用户购买商品下单业务逻辑流程
     *
     * @Param:
     * @Return:
     */
    @PostMapping("/buy")
    ObjectResponse handleBusiness(@RequestBody BusinessDTO businessDTO) {
        LOGGER.info("请求参数：{}", businessDTO.toString());
        return businessService.handleBusiness(businessDTO);
    }
}
