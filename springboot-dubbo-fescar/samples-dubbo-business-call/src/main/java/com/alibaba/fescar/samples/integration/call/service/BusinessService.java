package com.alibaba.fescar.samples.integration.call.service;

import com.alibaba.fescar.samples.integration.common.dto.BusinessDTO;
import com.alibaba.fescar.samples.integration.common.response.ObjectResponse;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/14 17:17
 */
public interface BusinessService {

    ObjectResponse handleBusiness(BusinessDTO businessDTO);
}
