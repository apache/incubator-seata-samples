package com.alibaba.fescar.samples.tcc.sofarpc.service;

import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.samples.tcc.sofarpc.action.TccActionOne;
import com.alibaba.fescar.samples.tcc.sofarpc.action.TccActionTwo;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;

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
        result = tccActionTwo.prepare(null, "two");
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
        result = tccActionTwo.prepare(null, "two");
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
