package com.demo.modules.product.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface CompanyProductAction {
    @TwoPhaseBusinessAction(name = "order-decuct",commitMethod = "commit",rollbackMethod = "cancel")
    void deduct(@BusinessActionContextParameter(paramName = "id") Long id);
    boolean commit(BusinessActionContext businessActionContext);
    boolean cancel(BusinessActionContext businessActionContext);
}
