package io.seata.samples.storage.service;

import io.seata.samples.storage.persistence.Storage;
import io.seata.samples.storage.persistence.StorageMapper;
import io.seata.spring.annotation.GlobalLock;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class StorageService {


    @Autowired
    private StorageMapper storageMapper;
    @Autowired
    private DataSource dataSource;

    public void deduct(String commodityCode, int count) {
        Storage storage = storageMapper.findByCommodityCode(commodityCode);
        storage.setCount(storage.getCount() - count);
        storageMapper.updateById(storage);
    }

    @GlobalLock
    public Storage get(Integer id) {
        return storageMapper.selectById(id);
    }

    /**
     * 0.8.0 release
     *
     * @throws SQLException
     */
    @GlobalTransactional
    public void batchUpdate() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            String sql = "update storage_tbl set count = ?" +
                "    where id = ? and commodity_code = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 100);
            preparedStatement.setLong(2, 1);
            preparedStatement.setString(3, "2001");
            preparedStatement.addBatch();
            preparedStatement.setInt(1, 200);
            preparedStatement.setLong(2, 2);
            preparedStatement.setString(3, "2002");
            preparedStatement.addBatch();
            preparedStatement.setInt(1, 300);
            preparedStatement.setLong(2, 3);
            preparedStatement.setString(3, "2003");
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            connection.commit();
            System.out.println(1 / 0);
        } catch (Exception e) {
            throw e;
        } finally {
            connection.close();
            preparedStatement.close();
        }
    }

    /**
     * 0.8.0 release
     *
     * @throws SQLException
     */
    @GlobalTransactional
    public void batchDelete() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            String sql = "delete from storage_tbl where  count = ? and commodity_code = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 11);
            preparedStatement.setString(2, "2001");
            preparedStatement.addBatch();
            preparedStatement.setInt(1, 22);
            preparedStatement.setString(2, "2002");
            preparedStatement.addBatch();
            preparedStatement.setInt(1, 33);
            preparedStatement.setString(2, "2003");
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            connection.commit();
            System.out.println(1 / 0);
        } catch (Exception e) {
            throw e;
        } finally {
            connection.close();
            preparedStatement.close();
        }
    }
}
