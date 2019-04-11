package com.alibaba.seata.sample.service;

import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.alibaba.seata.sample.feign.OrderFeignClient;
import com.alibaba.seata.sample.feign.StorageFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019-04-05
 */
@Service
public class BusinessService {

    @Autowired
    private StorageFeignClient storageFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 减库存，下订单
     * @param userId
     * @param commodityCode
     * @param orderCount
     */
    @GlobalTransactional
    public void purchase(String userId, String commodityCode, int orderCount){
        storageFeignClient.deduct(commodityCode, orderCount);

        orderFeignClient.create(userId, commodityCode, orderCount);
    }
}
