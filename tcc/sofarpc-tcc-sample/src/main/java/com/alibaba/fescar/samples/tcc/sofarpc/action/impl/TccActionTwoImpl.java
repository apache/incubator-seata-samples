package com.alibaba.fescar.samples.tcc.sofarpc.action.impl;

import com.alibaba.fescar.rm.tcc.api.BusinessActionContext;
import com.alibaba.fescar.samples.tcc.action.ResultHolder;
import com.alibaba.fescar.samples.tcc.sofarpc.action.TccActionTwo;

/**
 * @author zhangsen
 */
public class TccActionTwoImpl implements TccActionTwo {

    @Override
    public boolean prepare(BusinessActionContext actionContext, String b) {
        String txId = actionContext.getXid();
        System.out.println("TccActionTwo prepare, xid:" + txId);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String txId = actionContext.getXid();
        System.out.println("TccActionTwo commit, xid:" + txId);
        ResultHolder.setActionTwoResult(txId, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String txId = actionContext.getXid();
        System.out.println("TccActionTwo rollback, xid:" + txId);
        ResultHolder.setActionTwoResult(txId, "R");
        return true;
    }

}
