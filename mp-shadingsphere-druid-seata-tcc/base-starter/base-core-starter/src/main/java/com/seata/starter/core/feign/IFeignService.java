package com.seata.starter.core.feign;

public interface IFeignService {

    <T> T newInstance(Class<T> apiType, String name);
}
