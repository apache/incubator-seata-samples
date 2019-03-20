package com.alibaba.fescar.samples.tcc.sofarpc.action;

import com.alibaba.fescar.rm.tcc.api.BusinessActionContext;
import com.alibaba.fescar.rm.tcc.api.LocalTCC;
import com.alibaba.fescar.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * The interface Tcc action two.
 *
 * @author zhangsen
 */
public interface TccActionTwo {

    /**
     * Prepare boolean.
     *
     * @param actionContext the action context
     * @param b             the b
     * @return the boolean
     */
    @TwoPhaseBusinessAction(name = "SofaRpcTccActionTwo" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, String b);

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
