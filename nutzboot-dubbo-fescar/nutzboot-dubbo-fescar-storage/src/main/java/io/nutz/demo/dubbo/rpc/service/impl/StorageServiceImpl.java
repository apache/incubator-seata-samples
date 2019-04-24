package io.nutz.demo.dubbo.rpc.service.impl;

import javax.sql.DataSource;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fescar.core.context.RootContext;

import io.nutz.demo.bean.Storage;
import io.nutz.demo.dubbo.rpc.service.StorageService;

@IocBean
@Service(interfaceClass=StorageService.class)
public class StorageServiceImpl implements StorageService {

    private static final Log log = Logs.get();

    @Inject
    private Dao dao;
    
    @Inject
    private DataSource dataSource;

    @Override
    public void deduct(String commodityCode, int count) {
        log.info("Storage Service Begin ... xid: " + RootContext.getXID());
        log.infof("Deducting inventory SQL: update storage_tbl set count = count - %s where commodity_code = %s", count, commodityCode);

        dao.update(Storage.class, Chain.makeSpecial("count", "-" + count), Cnd.where("commodityCode", "=", commodityCode));
        log.info("Storage Service End ... ");

    }

}