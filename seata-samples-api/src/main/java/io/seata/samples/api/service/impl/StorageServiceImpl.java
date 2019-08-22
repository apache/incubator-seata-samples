package io.seata.samples.api.service.impl;

import java.sql.SQLException;

import io.seata.samples.api.service.AbstractDataCheck;
import io.seata.samples.api.service.StorageService;
import io.seata.samples.api.utils.DataSourceUtil;

/**
 * The type Storage service.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public class StorageServiceImpl extends AbstractDataCheck implements StorageService {
    /**
     * The constant DB_KEY.
     */
    public static final String DB_KEY = "storage";

    @Override
    public void deduct(String commodityCode, int count) throws SQLException {
        String sql = "update storage_tbl set count = count - " + count + " where commodity_code = '" + commodityCode
            + "'";
        DataSourceUtil.executeUpdate(DB_KEY, sql);
    }

    @Override
    public void reset(String key, String value) throws SQLException {
        String deleteSql = "delete from storage_tbl where commodity_code = '" + key + "'";
        String insertSql = "insert into storage_tbl(commodity_code, count) values ('" + key + "', " + value + ")";
        DataSourceUtil.executeUpdate(DB_KEY, deleteSql);
        DataSourceUtil.executeUpdate(DB_KEY, insertSql);
    }

    @Override
    public int doNegativeCheck(String field, String id) throws SQLException {
        String checkSql = "select " + field + " from storage_tbl where commodity_code='" + id + "'";
        String result = DataSourceUtil.getSingleResult(DB_KEY, checkSql);
        return Integer.parseInt(result);
    }
}
