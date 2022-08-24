/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.edas.tcc.activity;

import java.util.ArrayList;
import java.util.List;

import io.seata.core.context.RootContext;
import io.seata.edas.tcc.action.ActionOne;
import io.seata.edas.tcc.action.ActionTwo;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * @author zhangsen
 * @data 2020-02-12
 */
public class ActivityServiceImpl {

    private ActionOne actionOne;

    private ActionTwo actionTwo;

    @GlobalTransactional
    public String doActivity(boolean commit) {
        //第一个TCC 事务参与者
        boolean result = actionOne.prepare(null, 1);
        if (!commit || !result) {
            throw new RuntimeException("TccActionOne failed.");
        }

        //第二个TCC 事务参与者
        List list = new ArrayList();
        list.add("c1");
        list.add("c2");
        result = actionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }

        return RootContext.getXID();
    }

    public void setActionOne(ActionOne actionOne) {
        this.actionOne = actionOne;
    }

    public void setActionTwo(ActionTwo actionTwo) {
        this.actionTwo = actionTwo;
    }

}

