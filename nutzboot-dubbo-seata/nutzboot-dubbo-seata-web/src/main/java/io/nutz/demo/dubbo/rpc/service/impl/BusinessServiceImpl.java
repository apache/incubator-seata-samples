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
package io.nutz.demo.dubbo.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;;

import io.nutz.demo.dubbo.rpc.service.BusinessService;
import io.nutz.demo.dubbo.rpc.service.OrderService;
import io.nutz.demo.dubbo.rpc.service.StockService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean
public class BusinessServiceImpl implements BusinessService {

    private static final Log log = Logs.get();

    @Inject
    @Reference
    private StockService stockService;

    @Inject
    @Reference
    private OrderService orderService;

    /**
     * purchase
     */
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    @Override
    public void purchase(String userId, String commodityCode, int orderCount, boolean dofail) {
        log.info("purchase begin ... xid: " + RootContext.getXID());

        stockService.deduct(commodityCode, orderCount);
        orderService.create(userId, commodityCode, orderCount);

        // for test
        if (dofail) {
            throw new RuntimeException("just make it failed");
        }
    }
}