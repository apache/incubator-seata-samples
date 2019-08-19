package io.seata.samples.business.service;

import io.seata.core.context.RootContext;
import io.seata.samples.business.client.OrderClient;
import io.seata.samples.business.client.StorageClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    @Autowired
    private StorageClient storageClient;
    @Autowired
    private OrderClient orderClient;

    /**
     * 减库存，下订单
     *
     * @param userId
     * @param commodityCode
     * @param orderCount
     */
    @GlobalTransactional
    public void purchase(String userId, String commodityCode, int orderCount) {
        LOGGER.info("purchase begin ... xid: " + RootContext.getXID());
        storageClient.deduct(commodityCode, orderCount);
        orderClient.create(userId, commodityCode, orderCount);
    }
}
