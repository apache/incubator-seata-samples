package io.seata.samples.api.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import io.seata.rm.datasource.DataSourceProxy;

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
        DataSource dataSource = new DruidDataSource();
        ((DruidDataSource)dataSource).setDriverClassName(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, driverKey));
        ((DruidDataSource)dataSource).setUrl(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, urlKey));
        ((DruidDataSource)dataSource).setUsername(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, userNameKey));
        ((DruidDataSource)dataSource).setPassword(PropertiesUtil.getPropertieValue(JDBC_PRO_PATH, pwdKey));
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
        DATA_SOURCE_MAP.putIfAbsent(name, getDataSource(name));
        return DATA_SOURCE_MAP.get(name).getConnection();
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
        int result = 0;
        try {
            connection = getConnection(name);
            statement = connection.createStatement();
            result = statement.executeUpdate(sql);

        } catch (SQLException exx) {
            //todo
            throw exx;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exx) {
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
        String result = null;
        try {
            connection = getConnection(name);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            result = resultSet.getString(1);
        } catch (SQLException exx) {
            //todo
            throw exx;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exx) {
            }
        }
        return result;
    }
}
