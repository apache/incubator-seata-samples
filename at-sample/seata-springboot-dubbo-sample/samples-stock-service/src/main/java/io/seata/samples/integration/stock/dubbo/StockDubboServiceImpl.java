package io.seata.samples.integration.stock.dubbo;

import io.seata.core.context.RootContext;
import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.dubbo.StockDubboService;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.stock.service.ITStockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/23 16:13
 */
@DubboService(version = "1.0.0", protocol = "${dubbo.protocol.id}", application = "${dubbo.application.id}",
    registry = "${dubbo.registry.id}", timeout = 3000)
@Slf4j
public class StockDubboServiceImpl implements StockDubboService {

    @Autowired
    private ITStockService stockService;

    @Override
    public ObjectResponse decreaseStock(CommodityDTO commodityDTO) {
        log.info("全局事务id ：" + RootContext.getXID());
        return stockService.decreaseStock(commodityDTO);
    }
}
