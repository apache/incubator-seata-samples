package io.nutz.demo.dubbo.rpc.service;

public interface StockService {

    /**
     * deduct stock count
     */
    void deduct(String commodityCode, int count);
}