package com.seata.business.service;


import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface BusinessService{


    /**
     * 执行资源检查及预留操作 开启防悬挂能力
     */
    @TwoPhaseBusinessAction(name = "prepareBuy", commitMethod = "commit", rollbackMethod = "rollback",useTCCFence = true)
    public String prepareBuy(@BusinessActionContextParameter(paramName = "userId")String userId, @BusinessActionContextParameter(paramName = "commodityCode")String commodityCode,@BusinessActionContextParameter(paramName = "count") int count);

    /**
     * 全局事物进行提交
     */
    boolean commit(BusinessActionContext actionContext);

    /**
     * 全局事务进行回滚
     */
    boolean rollback(BusinessActionContext actionContext);

}
