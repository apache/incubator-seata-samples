package com.alibaba.fescar.samples.tcc.dubbo.action;

import com.alibaba.fescar.rm.tcc.api.BusinessActionContext;
import com.alibaba.fescar.rm.tcc.api.BusinessActionContextParameter;
import com.alibaba.fescar.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author zhangsen
 */
public interface TccActionOne {

    @TwoPhaseBusinessAction(name = "DubboTccActionOne" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext,
                           @BusinessActionContextParameter(paramName = "a") int a);

    public boolean commit(BusinessActionContext actionContext);

    public boolean rollback(BusinessActionContext actionContext);
}
