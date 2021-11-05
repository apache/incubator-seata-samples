package io.nutz.demo.dubbo.rpc.service.impl;

import javax.sql.DataSource;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fescar.core.context.RootContext;

import io.nutz.demo.bean.Stock;
import io.nutz.demo.dubbo.rpc.service.StockService;
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