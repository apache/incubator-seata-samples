package io.seata.samples.api.service;

import java.sql.SQLException;

/**
 * The type Abstract data check.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public abstract class AbstractDataCheck {

    /**
     * Valid negative check boolean.
     *
     * @param field the field
     * @param id    the id
     * @return the boolean
     */
    public boolean validNegativeCheck(String field, String id) throws SQLException {
        return doNegativeCheck(field, id) >= 0;
    }

    /**
     * Do negative check int.
     *
     * @param field the field
     * @param id    the id
     * @return the int
     * @throws SQLException the sql exception
     */
    public abstract int doNegativeCheck(String field, String id) throws SQLException;
}
