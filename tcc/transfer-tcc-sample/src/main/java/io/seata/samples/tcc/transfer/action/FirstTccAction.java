package io.seata.samples.tcc.transfer.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * TCC参与者：扣钱
 *
 * @author zhangsen
 */
public interface FirstTccAction {
	
	/**
     * 一阶段方法
     * 
     * @param businessActionContext
     * @param accountNo
     * @param amount
     */
    @TwoPhaseBusinessAction(name = "firstTccAction", commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepareMinus(BusinessActionContext businessActionContext,
                                @BusinessActionContextParameter(paramName = "accountNo") String accountNo,
                                @BusinessActionContextParameter(paramName = "amount") double amount);

    /**
     * 二阶段提交
     * @param businessActionContext
     * @return
     */
    public boolean commit(BusinessActionContext businessActionContext);

    /**
     * 二阶段回滚
     * @param businessActionContext
     * @return
     */
    public boolean rollback(BusinessActionContext businessActionContext);
}
