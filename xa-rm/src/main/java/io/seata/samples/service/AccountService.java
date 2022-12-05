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
package io.seata.samples.service;

import com.alibaba.fastjson.JSON;
import io.seata.core.context.RootContext;
import io.seata.samples.TestDatas;
import io.seata.samples.bean.Account;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from sys_account");
        jdbcTemplate.update("delete from sys_order");
        jdbcTemplate.update("delete from sys_stock");
        jdbcTemplate.update(
                "insert into sys_account(id,balance) values( " + TestDatas.USER_ID + "," + TestDatas.INIT_BALANCE + ")");
        jdbcTemplate.update(
                "insert into sys_stock(id,quantity,price) values(" + TestDatas.STOCK_ID + "," + TestDatas.STOCK_QUANTITY
                        + "," + TestDatas.STOCK_PRICE + ")");
    }

    @GlobalTransactional
    public boolean reduce(long accountId, BigDecimal money) {
        String xid = RootContext.getXID();
        LOGGER.info("reduce account balance in transaction: " + xid);
        int result = jdbcTemplate.update("update sys_account set balance = balance - ? where id = ? and balance >= ?",
            new Object[] {money, accountId, money});
        if (result <= 0) {
            throw new RuntimeException("Not Enough Money ...");
        }
        return true;
    }

    @GlobalTransactional
    public boolean increase(long accountId, BigDecimal money) {
        String xid = RootContext.getXID();
        LOGGER.info("reduce account balance in transaction: " + xid);
        int result = jdbcTemplate.update("update sys_account set balance = balance + ? where id = ?",
            new Object[] {money, accountId});
        if (result <= 0) {
            throw new RuntimeException("Not Enough Money ...");
        }
        return true;
    }

    public Map<String, Object> getOne(long accountId) {
        Map<String, Object> account = jdbcTemplate.queryForMap("select * from sys_account where id = ?",
                accountId);
        return account;
    }

}
