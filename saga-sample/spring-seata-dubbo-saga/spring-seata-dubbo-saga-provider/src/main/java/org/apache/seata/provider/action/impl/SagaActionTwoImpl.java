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

import org.apache.seata.rm.tcc.api.BusinessActionContextParameter;
import seata.util.ResultHolder;
import org.apache.seata.provider.action.SagaActionTwo;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.saga.rm.api.CompensationBusinessAction;
import org.springframework.util.Assert;

import java.util.List;

/**
 * SagaActionTwoImpl
 */
public class SagaActionTwoImpl implements SagaActionTwo {


    @Override
    @CompensationBusinessAction(name = "DubboTccActionTwo", compensationMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext, @BusinessActionContextParameter(paramName = "b") String b, @BusinessActionContextParameter(paramName = "c", index = 1) List list) {
        String xid = actionContext.getXid();
        System.out.println("TccActionTwo prepare, xid:" + xid + ", b:" + b + ", c:" + list.get(1));
        ResultHolder.setActionTwoResult(xid, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        Assert.isTrue(actionContext.getActionContext("b") != null);
        Assert.isTrue(actionContext.getActionContext("c") != null);
        System.out.println(
                "TccActionTwo rollback, xid:" + xid + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext
                        .getActionContext("c"));
        ResultHolder.setActionTwoResult(xid, "R");
        return true;
    }
}
