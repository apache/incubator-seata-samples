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
package org.apache.seata.service.impl;

import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.apache.seata.controller.TestData;
import org.apache.seata.feign.OrderFeignClient;
import org.apache.seata.feign.StockFeignClient;
import org.apache.seata.service.BusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class BusinessServiceImpl implements BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    @Resource
    private StockFeignClient stockFeignClient;
    @Resource
    private OrderFeignClient orderFeignClient;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "springboot-feign-seata-xa")
    public void purchase(String userId, String commodityCode, int orderCount, boolean forceRollback) {
        LOGGER.info("purchase begin ... xid: " + RootContext.getXID());
        stockFeignClient.deduct(commodityCode, orderCount);
        orderFeignClient.create(userId, commodityCode, orderCount);
        if (forceRollback) {
            throw new RuntimeException("force rollback!");
        }
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "springboot-feign-seata-xa-commit")
    public void purchaseCommit(String userId, String commodityCode, int orderCount) {
        LOGGER.info("purchaseCommit begin ... xid: " + RootContext.getXID());
        stockFeignClient.deduct(commodityCode, orderCount);
        orderFeignClient.create(userId, commodityCode, orderCount);
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "springboot-feign-seata-xa-rollback")
    public void purchaseRollback(String userId, String commodityCode, int orderCount) {
        LOGGER.info("purchaseRollback begin ... xid: " + RootContext.getXID());
        stockFeignClient.deduct(commodityCode, orderCount);
        orderFeignClient.create(userId, commodityCode, orderCount);
        throw new RuntimeException("force rollback!");
    }

    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from account_tbl");
        jdbcTemplate.update("delete from order_tbl");
        jdbcTemplate.update("delete from stock_tbl");
        jdbcTemplate.update("insert into account_tbl(user_id,money) values('" + TestData.USER_ID + "','10000') ");
        jdbcTemplate.update(
                "insert into stock_tbl(commodity_code,count) values('" + TestData.COMMODITY_CODE + "','100') ");
    }

}