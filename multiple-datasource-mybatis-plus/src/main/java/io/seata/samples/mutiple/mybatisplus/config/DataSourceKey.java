package io.seata.samples.mutiple.mybatisplus.config;

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
     * Storage data source key.
     */
    STORAGE,
    /**
     * Pay data source key.
     */
    PAY,
}