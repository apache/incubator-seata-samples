package io.seata.edas.tcc.activity;

import io.seata.core.context.RootContext;
import io.seata.edas.tcc.action.ActionOne;
import io.seata.edas.tcc.action.ActionTwo;
import io.seata.spring.annotation.GlobalTransactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangsen
 * @data 2020-02-12
 */
public class ActivityServiceImpl {

    private ActionOne actionOne;

    private ActionTwo actionTwo;

    @GlobalTransactional
    public String doActivity(boolean commit){
        //第一个TCC 事务参与者
        boolean result = actionOne.prepare(null, 1);
        if(!commit || !result){
            throw new RuntimeException("TccActionOne failed.");
        }

        //第二个TCC 事务参与者
        List list = new ArrayList();
        list.add("c1");
        list.add("c2");
        result = actionTwo.prepare(null, "two", list);
        if(!result){
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

