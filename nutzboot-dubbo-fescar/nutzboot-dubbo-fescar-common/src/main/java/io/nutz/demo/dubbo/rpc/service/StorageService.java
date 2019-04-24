package io.nutz.demo.dubbo.rpc.service;
public interface StorageService {

    /**
     * deduct storage count
     */
    void deduct(String commodityCode, int count);
}