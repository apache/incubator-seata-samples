package io.seata.samples.integration.call.service;

import io.seata.samples.integration.common.dto.BusinessDTO;
import io.seata.samples.integration.common.response.ObjectResponse;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/14 17:17
 */
public interface BusinessService {

    ObjectResponse handleBusiness(BusinessDTO businessDTO);
}
