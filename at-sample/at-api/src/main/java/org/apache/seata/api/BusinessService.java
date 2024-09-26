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
package org.apache.seata.api;

import org.apache.seata.core.exception.TransactionException;
import org.apache.seata.core.rpc.netty.RmNettyRemotingClient;
import org.apache.seata.rm.RMClient;
import org.apache.seata.tm.TMClient;
import org.apache.seata.tm.api.GlobalTransaction;
import org.apache.seata.tm.api.GlobalTransactionContext;
import org.apache.seata.api.service.OrderService;
import org.apache.seata.api.service.impl.AccountServiceImpl;
import org.apache.seata.api.service.impl.OrderServiceImpl;
import org.apache.seata.api.service.impl.StorageServiceImpl;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The type Bussiness.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21 使用api 构建 单体应用多数据源分布式事务 非spring环境
 */
public class BusinessService {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws SQLException         the sql exception
     * @throws TransactionException the transaction exception
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws SQLException, TransactionException, InterruptedException {
        // init tm client and rm client, only once
        String applicationId = "api";
        String txServiceGroup = "my_test_tx_group";
        String outPutRes = "";
        boolean isInE2ETest = isInE2ETest();
        TMClient.init(applicationId, txServiceGroup);
        RMClient.init(applicationId, txServiceGroup);

        RmNettyRemotingClient rmNettyRemotingClient = RmNettyRemotingClient.getInstance();
        Class<? extends RmNettyRemotingClient> rmRemoteClass = rmNettyRemotingClient.getClass();
        ReflectionUtils.doWithFields(rmRemoteClass, field -> {
            if (field.getName().equals("clientChannelManager")) {
                field.setAccessible(true);
                //channelManger
                Object o = field.get(rmNettyRemotingClient);
                Method reconnect;
                try {
                    reconnect = o.getClass().getDeclaredMethod("reconnect", String.class);
                    reconnect.setAccessible(true);
                    reconnect.invoke(o, "my_test_tx_group");
                } catch (Exception e) {
                    throw new RuntimeException("reconnect failed!", e);
                }
            }
        });

        String userId = "U100001";
        String commodityCode = "C00321";
        int commodityCount = 100;
        int money = 999;
        AccountServiceImpl accountService = new AccountServiceImpl();
        StorageServiceImpl stockService = new StorageServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        orderService.setAccountService(accountService);

        //reset data
        accountService.reset(userId, String.valueOf(money));
        stockService.reset(commodityCode, String.valueOf(commodityCount));
        orderService.reset(null, null);

        //trx
        GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();
        try {
            tx.begin(60000, "testBiz");
            System.out.println("begin trx, xid is " + tx.getXid());

            //biz operate 3 dataSources
            //set >=5 will be rollback(200*5>999) else will be commit
            int opCount = 5;
            stockService.deduct(commodityCode, opCount);
            orderService.create(userId, commodityCode, opCount);

            //check data if negative
            boolean needCommit = stockService.validNegativeCheck("count", commodityCode)
                    && accountService.validNegativeCheck("money", userId);

            //if data negative rollback else commit
            if (needCommit) {
                tx.commit();
                outPutRes = "{\"res\": \"commit\"}";
            } else {
                System.out.println("rollback trx, cause: data negative, xid is " + tx.getXid());
                tx.rollback();
                outPutRes = "{\"res\": \"rollback\"}";
            }
        } catch (Exception exx) {
            System.out.println("rollback trx, cause: " + exx.getMessage() + " , xid is " + tx.getXid());
            tx.rollback();
            if (isInE2ETest) {
                writeE2EResFile(exx.getMessage());
            }
            throw exx;
        }
        if (isInE2ETest) {
            writeE2EResFile(outPutRes);
        }
        TimeUnit.SECONDS.sleep(100);

    }

    private static void writeE2EResFile(String outPutRes) throws InterruptedException {
        try {
            Files.write(Paths.get("result.yaml"), outPutRes.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(outPutRes);
        TimeUnit.MINUTES.sleep(2);
    }

    public static boolean isInE2ETest() {
        Map<String, String> envs = System.getenv();
        String env = envs.getOrDefault("E2E_ENV", "");
        return "open".equals(env);
    }
}