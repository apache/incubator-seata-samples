package com.alibaba.fescar.samples.tcc.dubbo.action.impl;

import com.alibaba.fescar.rm.tcc.api.BusinessActionContext;
import com.alibaba.fescar.samples.tcc.dubbo.action.ResultHolder;
import com.alibaba.fescar.samples.tcc.dubbo.action.TccActionTwo;

/**
 * @author zhangsen
 */
public class TccActionTwoImpl implements TccActionTwo {

    @Override
    public boolean prepare(BusinessActionContext actionContext, String b) {
        String txId = actionContext.getTxId();
        System.out.println("TccActionTwo prepare, txId:" + txId);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String txId = actionContext.getTxId();
        System.out.println("TccActionTwo commit, txId:" + txId);
        ResultHolder.setActionTwoResult(txId, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String txId = actionContext.getTxId();
        System.out.println("TccActionTwo rollback, txId:" + txId);
        ResultHolder.setActionTwoResult(txId, "R");
        return true;
    }

}
