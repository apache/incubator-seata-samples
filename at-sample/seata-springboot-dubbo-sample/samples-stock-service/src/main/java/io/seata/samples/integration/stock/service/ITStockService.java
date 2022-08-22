package io.seata.samples.integration.stock.service;

import com.baomidou.mybatisplus.service.IService;
import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.stock.entity.TStock;

/**
 * 仓库服务
 *
 * * @author lidong
 *
 * @since 2019-09-04
 */
public interface ITStockService extends IService<TStock> {

    /**
     * 扣减库存
     */
    ObjectResponse decreaseStock(CommodityDTO commodityDTO);
}
