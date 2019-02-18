package com.alibaba.fescar.samples.integration.storage.service;

import com.alibaba.fescar.samples.integration.common.dto.CommodityDTO;
import com.alibaba.fescar.samples.integration.common.response.ObjectResponse;
import com.alibaba.fescar.samples.integration.storage.entity.TStorage;
import com.baomidou.mybatisplus.service.IService;

/**
 * 仓库服务
 * @author heshouyou
 * @since 2019-01-13
 */
public interface ITStorageService extends IService<TStorage> {

    /**
     * 扣减库存
     */
    ObjectResponse decreaseStorage(CommodityDTO commodityDTO);
}
