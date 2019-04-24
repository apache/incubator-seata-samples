package io.nutz.demo.dubbo.rpc.service;

public interface BusinessService {

    void purchase(String userId, String commodityCode, int orderCount, boolean dofail);
}
