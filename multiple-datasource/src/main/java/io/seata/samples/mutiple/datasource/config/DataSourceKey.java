package io.seata.samples.mutiple.datasource.config;

import lombok.Getter;

/**
 * @author HelloWoodes
 */

@Getter
public enum DataSourceKey {
    /**
     * Order data source key.
     */
    ORDER,
    /**
     * Stock data source key.
     */
    STOCK,
    /**
     * Pay data source key.
     */
    PAY,
}