package io.seata.samples.service;

import io.seata.samples.bean.Stock;

public interface StockService {
    Stock getStockById(Long id);
}
