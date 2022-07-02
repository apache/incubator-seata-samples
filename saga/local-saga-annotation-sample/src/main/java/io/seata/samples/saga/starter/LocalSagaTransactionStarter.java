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
package io.seata.samples.saga.starter;

import io.seata.common.util.StringUtils;
import io.seata.samples.saga.ApplicationKeeper;
import io.seata.samples.saga.action.BalanceAction;
import io.seata.samples.saga.action.InventoryAction;
import io.seata.samples.saga.action.ResultHolder;
import io.seata.samples.saga.service.SagaAnnotationTransactionService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Local saga annotation transaction starter.
 *
 * @author ruishansun
 */
public class LocalSagaTransactionStarter {

    /**
     * The Application context.
     */
    static AbstractApplicationContext applicationContext = null;

    /**
     * The Tcc transaction service.
     */
    static SagaAnnotationTransactionService sagaAnnotationTransactionService = null;

    static BalanceAction balanceAction;

    static InventoryAction inventoryAction;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        applicationContext = new ClassPathXmlApplicationContext(new String[] {"spring/seata-saga.xml"});

        sagaAnnotationTransactionService = (SagaAnnotationTransactionService)applicationContext.getBean("sagaAnnotationTransactionService");

        balanceAction = (BalanceAction)applicationContext.getBean("balanceAction");
        inventoryAction = (InventoryAction)applicationContext.getBean("inventoryAction");

        //分布式事务提交demo
        transactionCommitDemo();

        //分布式事务回滚demo
        transactionRollbackDemo();

        new ApplicationKeeper(applicationContext).keep();
    }


    private static void transactionCommitDemo() throws InterruptedException {
        String txId = sagaAnnotationTransactionService.doTransactionCommit();
        System.out.println(txId);
        Assert.isTrue(StringUtils.isNotBlank(txId), "事务开启失败");

        Thread.sleep(1000L);

        Assert.isTrue("T".equals(ResultHolder.getActionOneResult(txId)), "balanceAction commit success");
        Assert.isTrue("T".equals(ResultHolder.getActionTwoResult(txId)), "inventoryAction commit success");

        System.out.println("transaction commit demo finish.");
    }

    private static void transactionRollbackDemo() throws InterruptedException {
        Map map = new HashMap(16);
        try {
            sagaAnnotationTransactionService.doTransactionRollback(map);
            Assert.isTrue(false, "分布式事务未回滚");
        } catch (Throwable t) {
            Assert.isTrue(true, "分布式事务异常回滚");
        }
        String txId = (String)map.get("xid");
        Thread.sleep(1000L);

        Assert.isTrue("R".equals(ResultHolder.getActionOneResult(txId)), "balanceAction commit failed");
        Assert.isTrue("R".equals(ResultHolder.getActionTwoResult(txId)), "inventoryAction commit failed");

        System.out.println("transaction rollback demo finish.");
    }
}
