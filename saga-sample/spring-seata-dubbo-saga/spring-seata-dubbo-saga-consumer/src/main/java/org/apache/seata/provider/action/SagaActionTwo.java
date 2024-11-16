package org.apache.seata.provider.action;

import org.apache.seata.rm.tcc.api.BusinessActionContext;

import java.util.List;

/**
 * @author wangte
 * Create At 2024/11/16
 */
public interface SagaActionTwo {

    /**
     * Prepare boolean.
     *
     * @param actionContext the action context
     * @param b             the b
     * @param list          the list
     * @return the boolean
     */
    boolean prepare(BusinessActionContext actionContext, String b, List list);

    /**
     * Rollback boolean.
     *
     * @param actionContext the action context
     * @return the boolean
     */
    public boolean rollback(BusinessActionContext actionContext);

}
