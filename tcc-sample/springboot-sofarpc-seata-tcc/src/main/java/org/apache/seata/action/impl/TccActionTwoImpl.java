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
package org.apache.seata.action.impl;

import com.alipay.sofa.runtime.api.annotation.SofaService;
import com.alipay.sofa.runtime.api.annotation.SofaServiceBinding;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.action.ResultHolder;
import org.apache.seata.action.TccActionTwo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Tcc action two.
 *
 * @author zhangsen
 */
@SofaService(interfaceType = TccActionTwo.class, bindings = { @SofaServiceBinding(bindingType = "bolt") })
@Service
public class TccActionTwoImpl implements TccActionTwo {

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
        ResultHolder.setActionTwoResult(xid, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println(
            "TccActionTwo rollback, xid:" + xid + ", b:" + actionContext.getActionContext("b") + ", c:" + actionContext
                .getActionContext("c"));
        ResultHolder.setActionTwoResult(xid, "R");
        return true;
    }

}
