package io.seata.samples.saga.action;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Balance Actions
 */
public interface BalanceAction {

    /**
     * reduce
     * @param businessKey
     * @param amount
     * @param params
     * @return
     */
    boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params);

    /**
     * compensateReduce
     * @param businessKey
     * @param params
     * @return
     */
    boolean compensateReduce(String businessKey, Map<String, Object> params);

}
