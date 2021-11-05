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
package io.seata.samples.integration.stock.dubbo;

import com.alibaba.dubbo.config.annotation.Service;

import io.seata.core.context.RootContext;
import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.dubbo.StockDubboService;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.stock.service.ITStockService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/23 16:13
 */
@Service(version = "1.0.0", protocol = "${dubbo.protocol.id}", application = "${dubbo.application.id}",
    registry = "${dubbo.registry.id}", timeout = 3000)
public class StockDubboServiceImpl implements StockDubboService {

    @Autowired
    private ITStockService stockService;

    @Override
    public ObjectResponse decreaseStock(CommodityDTO commodityDTO) {
        System.out.println("全局事务id ：" + RootContext.getXID());
        return stockService.decreaseStock(commodityDTO);
    }
}
