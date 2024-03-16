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

import org.apache.seata.api.service.AbstractDataCheck;
import org.apache.seata.api.service.StorageService;
import org.apache.seata.api.utils.DataSourceUtil;

import java.sql.SQLException;



/**
 * The type Stock service.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public class StorageServiceImpl extends AbstractDataCheck implements StorageService {
    /**
     * The constant DB_KEY.
     */
    public static final String DB_KEY = "stock";

    @Override
    public void deduct(String commodityCode, int count) throws SQLException {
        String sql = "update stock_tbl set count = count - " + count + " where commodity_code = '" + commodityCode
            + "'";
        DataSourceUtil.executeUpdate(DB_KEY, sql);
    }

    @Override
    public void reset(String key, String value) throws SQLException {
        String deleteSql = "delete from stock_tbl where commodity_code = '" + key + "'";
        String insertSql = "insert into stock_tbl(commodity_code, count) values ('" + key + "', " + value + ")";
        DataSourceUtil.executeUpdate(DB_KEY, deleteSql);
        DataSourceUtil.executeUpdate(DB_KEY, insertSql);
    }

    @Override
    public int doNegativeCheck(String field, String id) throws SQLException {
        String checkSql = "select " + field + " from stock_tbl where commodity_code='" + id + "'";
        String result = DataSourceUtil.getSingleResult(DB_KEY, checkSql);
        return Integer.parseInt(result);
    }
}