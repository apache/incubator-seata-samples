package com.demo.modules.order.action;

import com.demo.modules.order.entity.UserOrder;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface UserOrderTccAction {
    @TwoPhaseBusinessAction(name = "gene-order",commitMethod = "commit",rollbackMethod = "cancel")
    void geneOrder(UserOrder userOrder,
                   @BusinessActionContextParameter(paramName = "id")
                   Long id);
    boolean commit(BusinessActionContext businessActionContext);
    boolean cancel(BusinessActionContext businessActionContext);
}
