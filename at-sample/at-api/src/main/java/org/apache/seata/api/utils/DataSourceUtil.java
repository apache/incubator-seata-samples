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
package org.apache.seata.api.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.seata.rm.datasource.DataSourceProxy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.seata.api.BusinessService.isInE2ETest;

/**
 * The type Data source util.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public class DataSourceUtil {

    /**
     * The constant JDBC_PRO_PATH.
     */
    public static final String JDBC_PRO_PATH = "jdbc.properties";
    /**
     * The constant DATA_SOURCE_MAP.
     */
    public static final ConcurrentMap<String, DataSource> DATA_SOURCE_MAP = new ConcurrentHashMap<>();

    /**
     * Gets data source.
     *
     * @param name the name
     * @return the data source
     */
    public static DataSource getDataSource(String name) {
        String driverKey = "jdbc." + name + ".driver";
        String urlKey = "jdbc." + name + ".url";
        String userNameKey = "jdbc." + name + ".username";
        String pwdKey = "jdbc." + name + ".password";
        DruidDataSource dataSource = new DruidDataSource();
        if (isInE2ETest()) {
            dataSource.setDriverClassName(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, "e2e." + driverKey));
            dataSource.setUrl(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, "e2e." + urlKey));
            dataSource.setUsername(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, "e2e." + userNameKey));
            dataSource.setPassword(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, "e2e." + pwdKey));
        } else {
            dataSource.setDriverClassName(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, driverKey));
            dataSource.setUrl(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, urlKey));
            dataSource.setUsername(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, userNameKey));
            dataSource.setPassword(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, pwdKey));
        }
        return new DataSourceProxy(dataSource);
    }

    /**
     * Gets connection.
     *
     * @param name the name
     * @return the connection
     * @throws SQLException the sql exception
     */
    public static Connection getConnection(String name) throws SQLException {
        return DATA_SOURCE_MAP.computeIfAbsent(name, s -> getDataSource(name)).getConnection();
    }

    /**
     * Execute update int.
     *
     * @param name the name
     * @param sql  the sql
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int executeUpdate(String name, String sql) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        int result;
        try {
            connection = getConnection(name);
            statement = connection.createStatement();
            result = statement.executeUpdate(sql);

        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ignore) {
            }
        }
        return result;
    }

    /**
     * Gets single result.
     *
     * @param name the name
     * @param sql  the sql
     * @return the single result
     * @throws SQLException the sql exception
     */
    public static String getSingleResult(String name, String sql) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String result;
        try {
            connection = getConnection(name);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            result = resultSet.getString(1);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ignore) {
            }
        }
        return result;
    }
}