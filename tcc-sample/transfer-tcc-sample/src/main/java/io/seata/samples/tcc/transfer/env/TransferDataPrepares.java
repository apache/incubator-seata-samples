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
package io.seata.samples.tcc.transfer.env;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * 准备转账 demo 数据
 *
 * @author zhangsen
 */
public class TransferDataPrepares {

    /**
     * 扣钱账户 数据源
     */
    private DataSource fromAccountDataSource;

    /**
     * 加钱账户 数据源
     */
    private DataSource toAccountDataSource;

    /**
     * 初始化表结构和转账数据
     */
    public void init(double initAmount) throws SQLException {
        //初始化表结构
        prepareTable(fromAccountDataSource);
        prepareTable(toAccountDataSource);
        //初始化表数据
        prepareData(fromAccountDataSource, "A", initAmount);
        prepareData(fromAccountDataSource, "B", initAmount);

        prepareData(toAccountDataSource, "C", initAmount);
    }

    /**
     * 初始化账户数据
     *
     * @param dataSource
     * @param accountNo
     * @param amount
     */
    protected void prepareData(DataSource dataSource, String accountNo, double amount) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            String sql = "insert into account(account_no, amount, freezed_amount) values(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, accountNo);
            ps.setDouble(2, amount);
            ps.setDouble(3, 0);
            ps.executeUpdate();
            ps.close();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * 初始化表结构
     *
     * @param dataSource
     */
    protected void prepareTable(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            Statement s = conn.createStatement();
            try {
                s.execute("drop table business_activity");
            } catch (Exception e) {
            }
            s.execute("CREATE TABLE  business_activity  ( tx_id  varchar NOT NULL , gmt_create  TIMESTAMP , "
                + "gmt_modified  TIMESTAMP , instance_id  varchar  , business_type  varchar , "
                + "business_id  varchar  , state  varchar , app_name  varchar ,  timeout  int ,  "
                + "context  varchar , is_sync  varchar ) ");
            System.out.println("创建 business_activity 表成功");

            try {
                s.execute("drop table business_action");
            } catch (Exception e) {
            }
            s.execute("CREATE TABLE  business_action  (  tx_id  varchar , action_id  varchar  , gmt_create  "
                + "TIMESTAMP  , gmt_modified  TIMESTAMP , instance_id  varchar  , rs_id  varchar "
                + ", rs_desc  varchar , rs_type  smallint  , state  varchar , context  varchar "
                + " )");
            System.out.println("创建 business_action 表成功");

            //账户余额表
            try {
                s.execute("drop table account");
            } catch (Exception e) {
            }
            s.execute(
                "create table account(account_no varchar, amount DOUBLE,  freezed_amount DOUBLE)");
            System.out.println("创建account表成功");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setFromAccountDataSource(DataSource fromAccountDataSource) {
        this.fromAccountDataSource = fromAccountDataSource;
    }

    public void setToAccountDataSource(DataSource toAccountDataSource) {
        this.toAccountDataSource = toAccountDataSource;
    }
}
