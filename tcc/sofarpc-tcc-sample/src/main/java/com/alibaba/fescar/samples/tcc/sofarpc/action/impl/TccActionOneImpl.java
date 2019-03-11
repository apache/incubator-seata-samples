package com.alibaba.fescar.samples.tcc.sofarpc.action.impl;


import com.alibaba.fescar.rm.tcc.api.BusinessActionContext;
import com.alibaba.fescar.samples.tcc.action.ResultHolder;
import com.alibaba.fescar.samples.tcc.sofarpc.action.TccActionOne;

/**
 * @author zhangsen
 */
public class TccActionOneImpl implements TccActionOne {


    @Override
    public boolean prepare(BusinessActionContext actionContext, int a) {
        String txId = actionContext.getTxId();
        System.out.println("TccActionOne prepare, txId:" + txId);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String txId = actionContext.getTxId();
        System.out.println("TccActionOne commit, txId:" + txId);
        ResultHolder.setActionOneResult(txId, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String txId = actionContext.getTxId();
        System.out.println("TccActionOne rollback, txId:" + txId);
        ResultHolder.setActionOneResult(txId, "R");
        return true;
    }
}
