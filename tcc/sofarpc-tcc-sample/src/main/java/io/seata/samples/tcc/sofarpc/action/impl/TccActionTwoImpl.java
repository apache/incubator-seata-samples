package io.seata.samples.tcc.sofarpc.action.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.samples.tcc.sofarpc.action.ResultHolder;
import io.seata.samples.tcc.sofarpc.action.TccActionTwo;

/**
 * The type Tcc action two.
 *
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
