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
package org.apache.seata.api.service.impl;

import org.apache.seata.api.service.AccountService;
import org.apache.seata.api.service.OrderService;
import org.apache.seata.api.utils.DataSourceUtil;

import java.sql.SQLException;


/**
 * The type Order service.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public class OrderServiceImpl implements OrderService {

    /**
     * The constant DB_KEY.
     */
    public static final String DB_KEY = "order";
    private AccountService accountService;

    @Override
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void create(String userId, String commodityCode, Integer count) throws SQLException {
        int money = count * 200;
        String sql = "insert into order_tbl (user_id, commodity_code, count, money) values ('" + userId + "','"
            + commodityCode + "'," + count + "," + money + ")";
        DataSourceUtil.executeUpdate(DB_KEY, sql);
        accountService.reduce(userId, money);

    }

    @Override
    public void reset(String key, String value) throws SQLException {
        String deleteSql = "delete from order_tbl";
        DataSourceUtil.executeUpdate(DB_KEY, deleteSql);
    }
}