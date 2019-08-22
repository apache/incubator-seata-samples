package io.seata.samples.api.service;

import java.sql.SQLException;

/**
 * The interface Storage service.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public interface StorageService extends DataResetService {
    /**
     * Deduct.
     *
     * @param commodityCode the commodity code
     * @param count         the count
     * @throws SQLException the sql exception
     */
    void deduct(String commodityCode, int count) throws SQLException;
}
