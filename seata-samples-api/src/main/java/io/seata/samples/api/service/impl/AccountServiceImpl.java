package io.seata.samples.api.service.impl;

import java.sql.SQLException;

import io.seata.samples.api.service.AbstractDataCheck;
import io.seata.samples.api.service.AccountService;
import io.seata.samples.api.utils.DataSourceUtil;

/**
 * The type Account service.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public class AccountServiceImpl extends AbstractDataCheck implements AccountService {

    /**
     * The constant DB_KEY.
     */
    public static final String DB_KEY = "account";

    @Override
    public void reduce(String userId, int money) throws SQLException {
        String sql = "update account_tbl set money = money - " + money + " where user_id = '" + userId + "'";
        DataSourceUtil.executeUpdate(DB_KEY, sql);
    }

    @Override
    public int doNegativeCheck(String field, String id) throws SQLException {
        String checkSql = "select " + field + " from account_tbl where user_id='" + id + "'";
        String result = DataSourceUtil.getSingleResult(DB_KEY, checkSql);
        return Integer.parseInt(result);
    }

    @Override
    public void reset(String key, String value) throws SQLException {
        String deleteSql = "delete from account_tbl where user_id = '" + key + "'";
        String insertSql = "insert into account_tbl(user_id, money) values ('" + key + "', " + value + ")";
        DataSourceUtil.executeUpdate(DB_KEY, deleteSql);
        DataSourceUtil.executeUpdate(DB_KEY, insertSql);
    }
}
