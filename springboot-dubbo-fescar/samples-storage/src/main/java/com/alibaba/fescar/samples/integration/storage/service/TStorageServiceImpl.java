package com.alibaba.fescar.samples.integration.storage.service;

import com.alibaba.fescar.samples.integration.common.dto.CommodityDTO;
import com.alibaba.fescar.samples.integration.common.enums.RspStatusEnum;
import com.alibaba.fescar.samples.integration.common.response.ObjectResponse;
import com.alibaba.fescar.samples.integration.storage.entity.TStorage;
import com.alibaba.fescar.samples.integration.storage.mapper.TStorageMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  库存服务实现类
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
@Service
public class TStorageServiceImpl extends ServiceImpl<TStorageMapper, TStorage> implements ITStorageService {

    @Override
    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        int storage = baseMapper.decreaseStorage(commodityDTO.getCommodityCode(), commodityDTO.getCount());
        ObjectResponse<Object> response = new ObjectResponse<>();
        if (storage > 0){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    }
}
