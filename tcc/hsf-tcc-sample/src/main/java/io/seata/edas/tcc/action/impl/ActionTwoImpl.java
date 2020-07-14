package io.seata.edas.tcc.action.impl;

import io.seata.edas.tcc.action.ActionTwo;
import io.seata.rm.tcc.api.BusinessActionContext;

import java.util.List;

/**
 * The type Tcc action two.
 *
 * @author zhangsen
 */
public class ActionTwoImpl implements ActionTwo {

    @Override
    public boolean prepare(BusinessActionContext actionContext, String b, List list) {
        String xid = actionContext.getXid();
        System.out.println("TccActionTwo prepare, xid:" + xid + ", b:" + b + ", c:" + list.get(1));
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println(
            "TccActionTwo commit, xid:" + xid + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext
                .getActionContext("c"));
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println(
            "TccActionTwo rollback, xid:" + xid + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext
                .getActionContext("c"));
        return true;
    }

}
