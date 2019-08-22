package io.seata.samples.api.service;

import java.sql.SQLException;

/**
 * The interface Account service.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public interface AccountService extends DataResetService {
    /**
     * Reduce.
     *
     * @param userId the user id
     * @param money  the money
     * @throws SQLException the sql exception
     */
    void reduce(String userId, int money) throws SQLException;
}
