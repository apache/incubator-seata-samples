package io.seata.samples.service.impl;

import io.seata.samples.bean.Stock;
import io.seata.samples.mapper.StockMapper;
import io.seata.samples.service.StockService;
import org.springframework.stereotype.Service;

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
}
