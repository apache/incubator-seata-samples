package io.seata.edas.tcc.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

import java.util.List;

/**
 * @author zhangsen
 * @data 2020-02-12
 */
public interface ActionTwo {

    /**
     * Prepare boolean.
     *
     * @param actionContext the action context
     * @param b             the b
     * @param list          the list
     * @return the boolean
     */
    @TwoPhaseBusinessAction(name = "TccActionTwo" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, @BusinessActionContextParameter(paramName = "b") String b,
                           @BusinessActionContextParameter(paramName = "c", index = 1) List list);

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
