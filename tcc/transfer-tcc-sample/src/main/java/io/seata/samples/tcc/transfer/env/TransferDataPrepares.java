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
        }catch (Throwable t){
            t.printStackTrace();
        }finally {
            if(conn != null){
                conn.close();
            }
        }
    }

    /**
     * 初始化表结构
     * @param dataSource
     */
    protected void prepareTable(DataSource dataSource){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            Statement s = conn.createStatement();
            try {
                s.execute("drop table business_activity");
            } catch (Exception e) {
            }
            s.execute("CREATE TABLE  business_activity  ( tx_id  varchar(128) NOT NULL , gmt_create  TIMESTAMP(6) , gmt_modified  TIMESTAMP(6) , instance_id  varchar(128)  , business_type  varchar(32) , business_id  varchar(128)  , state  varchar(2) , app_name  varchar(32) ,  timeout  int(11) ,  context  varchar(2000) , is_sync  varchar(1) , PRIMARY KEY ( tx_id )) ");
            System.out.println("创建 business_activity 表成功");

            try {
                s.execute("drop table business_action");
            } catch (Exception e) {
            }
            s.execute("CREATE TABLE  business_action  (  tx_id  varchar(128) , action_id  varchar(64)  , gmt_create  TIMESTAMP(6)  , gmt_modified  TIMESTAMP(6) , instance_id  varchar(128)  , rs_id  varchar(128) , rs_desc  varchar(512) , rs_type  smallint(6)  , state  varchar(2) , context  varchar(2000) , PRIMARY KEY ( action_id ))");
            System.out.println("创建 business_action 表成功");

            //账户余额表
            try {
                s.execute("drop table account");
            } catch (Exception e) {
            }
            s.execute("create table account(account_no varchar(256), amount DOUBLE,  freezed_amount DOUBLE, PRIMARY KEY (account_no))");
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
