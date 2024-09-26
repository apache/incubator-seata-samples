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
package org.apache.seata.service.impl;

import org.apache.seata.core.context.RootContext;
import org.apache.seata.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * The type Stock service.
 *
 * @author jimin.jm @alibaba-inc.com
 */
@Service
public class StockServiceImpl implements StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void deduct(String commodityCode, int count) {
        LOGGER.info("Stock Service Begin ... xid: " + RootContext.getXID());
        LOGGER.info("Deducting inventory SQL: update stock_tbl set count = count - {} where commodity_code = {}", count,
                commodityCode);

        jdbcTemplate.update("update stock_tbl set count = count - ? where commodity_code = ?",
                new Object[]{count, commodityCode});
        LOGGER.info("Stock Service End ... ");

    }

}
