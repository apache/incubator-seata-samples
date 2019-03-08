package com.alibaba.fescar.samples.tcc.dubbo.service;

import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.samples.tcc.dubbo.action.TccActionOne;
import com.alibaba.fescar.samples.tcc.dubbo.action.TccActionTwo;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangsen
 */
public class TccTransactionService {

    private TccActionOne tccActionOne;

    private TccActionTwo tccActionTwo;

    /**
     * 发起分布式事务
     * @return
     */
    @GlobalTransactional
    public String doTransactionCommit(){
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if(!result){
            throw new RuntimeException("TccActionOne failed.");
        }
        List list = new ArrayList();
        list.add("c1");
        list.add("c2");
        result = tccActionTwo.prepare(null, "two", list);
        if(!result){
            throw new RuntimeException("TccActionTwo failed.");
        }
        return RootContext.getXID();
    }

    @GlobalTransactional
    public String doTransactionRollback(Map map){
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if(!result){
            throw new RuntimeException("TccActionOne failed.");
        }
        List list = new ArrayList();
        list.add("c1");
        list.add("c2");
        result = tccActionTwo.prepare(null, "two", list);
        if(!result){
            throw new RuntimeException("TccActionTwo failed.");
        }
        map.put("xid", RootContext.getXID());
        throw new RuntimeException("transacton rollback");
    }

    public void setTccActionOne(TccActionOne tccActionOne) {
        this.tccActionOne = tccActionOne;
    }

    public void setTccActionTwo(TccActionTwo tccActionTwo) {
        this.tccActionTwo = tccActionTwo;
    }
}
