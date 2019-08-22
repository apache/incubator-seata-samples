package io.seata.samples.api.service;

import java.sql.SQLException;

/**
 * The interface Data reset service.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public interface DataResetService {
    /**
     * Reset.
     *
     * @param key   the key
     * @param value the value
     * @throws SQLException the sql exception
     */
    void reset(String key, String value) throws SQLException;
}
