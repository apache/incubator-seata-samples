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

import javax.sql.DataSource;

import com.alibaba.dubbo.config.annotation.Service;

import io.nutz.demo.bean.Stock;
import io.nutz.demo.dubbo.rpc.service.StockService;
import io.seata.core.context.RootContext;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean
@Service(interfaceClass = StockService.class)
public class StockServiceImpl implements StockService {

    private static final Log log = Logs.get();

    @Inject
    private Dao dao;

    @Inject
    private DataSource dataSource;

    @Override
    public void deduct(String commodityCode, int count) {
        log.info("Stock Service Begin ... xid: " + RootContext.getXID());
        log.infof("Deducting inventory SQL: update stock_tbl set count = count - %s where commodity_code = %s", count,
            commodityCode);

        dao.update(Stock.class, Chain.makeSpecial("count", "-" + count),
            Cnd.where("commodityCode", "=", commodityCode));
        log.info("Stock Service End ... ");

    }

}