package io.seata.samples.service;

import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * 
 * @author 陈健斌
 * @date 2019/12/05
 */
public interface DemoService {

    /**
     * 
     * @return
     * @throws TransactionException
     */
    public Object testRollback() throws TransactionException;

    /**
     * 
     * @return
     * @throws TransactionException
     */
    @GlobalTransactional
    public Object testCommit() throws TransactionException;
}
