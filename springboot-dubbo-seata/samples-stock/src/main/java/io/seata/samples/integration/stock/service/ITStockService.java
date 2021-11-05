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
package io.seata.samples.integration.stock.service;

import com.baomidou.mybatisplus.service.IService;
import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.stock.entity.TStock;

/**
 * 仓库服务
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface ITStockService extends IService<TStock> {

    /**
     * 扣减库存
     */
    ObjectResponse decreaseStock(CommodityDTO commodityDTO);
}
