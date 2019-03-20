package com.alibaba.fescar.samples.tcc.action;

import com.alibaba.fescar.rm.tcc.api.BusinessActionContext;
import com.alibaba.fescar.rm.tcc.api.LocalTCC;
import com.alibaba.fescar.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * The interface Tcc action two.
 *
 * @author zhangsen
 */
@LocalTCC
public interface TccActionTwo {

    /**
     * Prepare boolean.
     *
     * @param actionContext the action context
     * @param b             the b
     * @return the boolean
     */
    @TwoPhaseBusinessAction(name = "TccActionTwo" , commitMethod = "commit", rollbackMethod = "rollback")
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
