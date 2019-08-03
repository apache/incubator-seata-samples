package io.seata.samples.tcc.sofarpc.action.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.samples.tcc.sofarpc.action.ResultHolder;
import io.seata.samples.tcc.sofarpc.action.TccActionOne;

/**
 * The type Tcc action one.
 *
 * @author zhangsen
 */
public class TccActionOneImpl implements TccActionOne {


    @Override
    public boolean prepare(BusinessActionContext actionContext, int a) {
        String txId = actionContext.getXid();
        System.out.println("TccActionOne prepare, txId:" + txId);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String txId = actionContext.getXid();
        System.out.println("TccActionOne commit, txId:" + txId);
        ResultHolder.setActionOneResult(txId, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String txId = actionContext.getXid();
        System.out.println("TccActionOne rollback, txId:" + txId);
        ResultHolder.setActionOneResult(txId, "R");
        return true;
    }
}
