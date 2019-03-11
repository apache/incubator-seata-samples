package com.alibaba.fescar.samples.tcc.dubbo.action;

import com.alibaba.fescar.rm.tcc.api.BusinessActionContext;
import com.alibaba.fescar.rm.tcc.api.BusinessActionContextParameter;
import com.alibaba.fescar.rm.tcc.api.TwoPhaseBusinessAction;

import java.util.List;

/**
 * @author zhangsen
 */
public interface TccActionTwo {

    @TwoPhaseBusinessAction(name = "DubboTccActionTwo" , commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, @BusinessActionContextParameter(paramName = "b") String b,
                           @BusinessActionContextParameter(paramName = "c",index = 1) List list);

    public boolean commit(BusinessActionContext actionContext);

    public boolean rollback(BusinessActionContext actionContext);

}
