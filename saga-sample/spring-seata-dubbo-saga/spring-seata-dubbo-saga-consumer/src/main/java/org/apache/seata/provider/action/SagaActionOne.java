package org.apache.seata.provider.action;

import org.apache.seata.rm.tcc.api.BusinessActionContext;

/**
 * @author wangte
 * Create At 2024/11/16
 */
public interface SagaActionOne {

    boolean prepare(BusinessActionContext actionContext, int a);

    boolean compensation(BusinessActionContext actionContext);
}
