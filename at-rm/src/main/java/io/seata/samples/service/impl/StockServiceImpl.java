/*
 *  Copyright 1999-2022 Seata.io Group.
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
package io.seata.samples.service.impl;

import io.seata.samples.bean.Stock;
import io.seata.samples.mapper.StockMapper;
import io.seata.samples.service.StockService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StockServiceImpl implements StockService {
    private final StockMapper stockMapper;

    public StockServiceImpl(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    @Override
    public Stock getStockById(Long id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("stock id should not be null");
        }
        return this.stockMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean addOrUpdateStockFail(BigDecimal quantity, BigDecimal price) {
        return this.stockMapper.addOrUpdateStock(quantity, price);
    }

    @Override
    public Integer addOrUpdateStockSuccess(Long stockId, BigDecimal quantity, BigDecimal price) {
        return this.stockMapper.addOrUpdateStock2(stockId, quantity, price);
    }
}
