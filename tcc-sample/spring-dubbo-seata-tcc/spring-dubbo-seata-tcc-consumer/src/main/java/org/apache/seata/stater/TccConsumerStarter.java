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
package org.apache.seata.stater;

import org.apache.seata.common.util.StringUtils;
import org.apache.seata.service.TccTransactionService;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import static org.apache.seata.e2e.E2EUtil.isInE2ETest;
import static org.apache.seata.e2e.E2EUtil.writeE2EResFile;

public class TccConsumerStarter {
    private static TccTransactionService tccTransactionService = null;

    public static void main(String[] args) throws Exception {
        new TccConsumerStarter().start0(args);
    }

    private void start0(String[] args) throws Exception {
        if (isInE2ETest()) {
            // wait provider
            Thread.sleep(5000);
        }

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setValidating(false);  // 关闭 XML 验证
        context.setConfigLocation("classpath:spring/seata-dubbo-reference.xml");
        context.refresh();
        tccTransactionService = (TccTransactionService)context.getBean("tccTransactionService");

        //分布式事务提交demo
        transactionCommitDemo();
        //分布式事务回滚demo
        transactionRollbackDemo();
    }

    private static void transactionCommitDemo() throws InterruptedException {
        String txId = tccTransactionService.doTransactionCommit();
        System.out.println(txId);
//        //wait commit
//        Thread.sleep(3000);

        Assert.isTrue(StringUtils.isNotEmpty(txId), "事务开启失败");

        if (isInE2ETest()) {
            String res =  "{\"res\": \"commit\"}";
            writeE2EResFile(res, "commit.yaml");
        }
        System.out.println("transaction commit demo finish.");
    }

    private static void transactionRollbackDemo() throws InterruptedException {
        try {
            tccTransactionService.doTransactionRollback();
            Assert.isTrue(false, "分布式事务未回滚");
        } catch (Throwable t) {
            Assert.isTrue(true, "分布式事务异常回滚");
        }

//        //wait rollback
//        Thread.sleep(3000);

        if (isInE2ETest()) {
            String res =  "{\"res\": \"rollback\"}";
            writeE2EResFile(res, "rollback.yaml");
        }
        System.out.println("transaction rollback demo finish.");
    }
}
