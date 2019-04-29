package io.nutz.demo.dubbo.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;

import io.nutz.demo.dubbo.rpc.service.BusinessService;
import io.nutz.demo.dubbo.rpc.service.OrderService;
import io.nutz.demo.dubbo.rpc.service.StorageService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean
public class BusinessServiceImpl implements BusinessService {

    private static final Log log = Logs.get();
    
    @Inject
    @Reference
    private StorageService storageService;

    
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
        
        storageService.deduct(commodityCode, orderCount);
        orderService.create(userId, commodityCode, orderCount);
        
        // for test
        if (dofail) {
            throw new RuntimeException("just make it failed");
        }
    }
}