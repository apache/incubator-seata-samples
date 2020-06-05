package io.seata.edas.tcc.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author zhangsen
 * @data 2020-02-12
 */
public interface ActionOne {


    /**
     * Prepare boolean.
     *
     * @param actionContext the action context
     * @param a             the a
     * @return the boolean
     */
    @TwoPhaseBusinessAction(name = "TccActionOne" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext,
                           @BusinessActionContextParameter(paramName = "a") int a);

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
