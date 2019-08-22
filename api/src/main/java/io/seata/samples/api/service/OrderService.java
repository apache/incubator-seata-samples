package io.seata.samples.api.service;

import java.sql.SQLException;

/**
 * The interface Order service.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public interface OrderService extends DataResetService {
    /**
     * Sets account service.
     *
     * @param accountService the account service
     */
    void setAccountService(AccountService accountService);

    /**
     * Create.
     *
     * @param userId        the user id
     * @param commodityCode the commodity code
     * @param count         the count
     * @throws SQLException the sql exception
     */
    void create(String userId, String commodityCode, Integer count) throws SQLException;
}
