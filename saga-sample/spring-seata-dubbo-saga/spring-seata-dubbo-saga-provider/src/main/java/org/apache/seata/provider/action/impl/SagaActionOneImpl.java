/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.seata.provider.action.impl;

import org.apache.seata.provider.action.SagaActionOne;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.rm.tcc.api.BusinessActionContextParameter;
import org.apache.seata.saga.rm.api.CompensationBusinessAction;
import org.springframework.util.Assert;
import seata.util.ResultHolder;

/**
 * SagaActionOneImpl
 */
public class SagaActionOneImpl implements SagaActionOne {

    @Override
    @CompensationBusinessAction(name = "DubboSagaActionOne", compensationMethod = "compensation")
    public boolean prepare(BusinessActionContext actionContext, @BusinessActionContextParameter(paramName = "a") int a) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne prepare, xid:" + xid + ", a:" + a);
        ResultHolder.setActionOneResult(xid, "T");
        return true;
    }

    @Override
    public boolean compensation(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        Assert.isTrue(actionContext.getActionContext("a") != null);
        System.out.println("TccActionOne rollback, xid:" + xid + ", a:" + actionContext.getActionContext("a"));
        ResultHolder.setActionOneResult(xid, "R");
        return true;
    }
}
