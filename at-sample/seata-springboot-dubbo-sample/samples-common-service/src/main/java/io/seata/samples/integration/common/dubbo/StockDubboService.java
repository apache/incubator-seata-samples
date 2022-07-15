package io.seata.samples.integration.common.dubbo;

import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.response.ObjectResponse;

/**
 * @Author: lidong
 * @Description 库存服务
 * @Date Created in 2019/9/5 16:22
 */
public interface StockDubboService {

    /**
     * 扣减库存
     */
    ObjectResponse decreaseStock(CommodityDTO commodityDTO);
}
