package com.alibaba.fescar.samples.tcc.action.impl;

import com.alibaba.fescar.rm.tcc.api.BusinessActionContext;
import com.alibaba.fescar.samples.tcc.action.ResultHolder;
import com.alibaba.fescar.samples.tcc.action.TccActionOne;

/**
 * @author zhangsen
 */
public class TccActionOneImpl implements TccActionOne {


    @Override
    public boolean prepare(BusinessActionContext actionContext, int a) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne prepare, xid:" + xid);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne commit, xid:" + xid);
        ResultHolder.setActionOneResult(xid, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne rollback, xid:" + xid);
        ResultHolder.setActionOneResult(xid, "R");
        return true;
    }
}
