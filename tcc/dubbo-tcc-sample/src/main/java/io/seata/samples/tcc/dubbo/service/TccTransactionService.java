package io.seata.samples.tcc.dubbo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.seata.core.context.RootContext;
import io.seata.samples.tcc.dubbo.action.TccActionOne;
import io.seata.samples.tcc.dubbo.action.TccActionTwo;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * The type Tcc transaction service.
 *
 * @author zhangsen
 */
public class TccTransactionService {

    private TccActionOne tccActionOne;

    private TccActionTwo tccActionTwo;

    /**
     * 发起分布式事务
     *
     * @return string string
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

    /**
     * Do transaction rollback string.
     *
     * @param map the map
     * @return the string
     */
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

    /**
     * Sets tcc action one.
     *
     * @param tccActionOne the tcc action one
     */
    public void setTccActionOne(TccActionOne tccActionOne) {
        this.tccActionOne = tccActionOne;
    }

    /**
     * Sets tcc action two.
     *
     * @param tccActionTwo the tcc action two
     */
    public void setTccActionTwo(TccActionTwo tccActionTwo) {
        this.tccActionTwo = tccActionTwo;
    }
}
