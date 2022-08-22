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
package io.seata.samples.tcc.dubbo.starter;

import java.util.HashMap;
import java.util.Map;

import io.seata.samples.jit.AbstractStarter;
import io.seata.samples.tcc.dubbo.service.TccTransactionService;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * The type Dubbo tcc transaction starter.
 *
 * @author ppf
 * @author zhangsen
 */
public class TccConsumerStarter extends AbstractStarter {
    static TccTransactionService tccTransactionService = null;

    public static void main(String[] args) throws Exception {
        new TccConsumerStarter().start0(args);
    }

    @Override
    protected void start0(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
            new String[] {"spring/seata-tcc.xml", "spring/seata-dubbo-reference.xml"});
        tccTransactionService = (TccTransactionService)applicationContext.getBean("tccTransactionService");

        //分布式事务提交demo
        transactionCommitDemo();
        //分布式事务回滚demo
        transactionRollbackDemo();
    }

    private static void transactionCommitDemo() throws InterruptedException {
        String txId = tccTransactionService.doTransactionCommit();
        System.out.println(txId);
        Assert.isTrue(StringUtils.isNotEmpty(txId), "事务开启失败");

        System.out.println("transaction commit demo finish.");
    }

    private static void transactionRollbackDemo() throws InterruptedException {
        Map map = new HashMap(16);
        try {
            tccTransactionService.doTransactionRollback(map);
            Assert.isTrue(false, "分布式事务未回滚");
        } catch (Throwable t) {
            Assert.isTrue(true, "分布式事务异常回滚");
        }
        String txId = (String)map.get("xid");
        System.out.println(txId);

        System.out.println("transaction rollback demo finish.");
    }
}
