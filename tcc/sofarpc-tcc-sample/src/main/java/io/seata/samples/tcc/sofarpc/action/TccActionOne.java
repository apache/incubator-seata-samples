package io.seata.samples.tcc.sofarpc.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * The interface Tcc action one.
 *
 * @author zhangsen
 */
public interface TccActionOne {

    /**
     * Prepare boolean.
     *
     * @param actionContext the action context
     * @param a             the a
     * @return the boolean
     */
    @TwoPhaseBusinessAction(name = "SofaRpcTccActionOne" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, int a);

    /**
     * Commit boolean.
     *
     * @param actionContext the action context
     * @return the boolean
     */
    public boolean commit(BusinessActionContext actionContext);

    /**
     * Rollback boolean.
     *
     * @param actionContext the action context
     * @return the boolean
     */
    public boolean rollback(BusinessActionContext actionContext);
}
