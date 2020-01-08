package io.seata.samples.integration.common.dubbo;

import io.seata.samples.integration.common.dto.OrderDTO;
import io.seata.samples.integration.common.response.ObjectResponse;

/**
 * @Author: lidong
 * @Description  订单服务接口
 * @Date Created in 2019/9/5 16:28
 */
public interface OrderDubboService {

    /**
     * 创建订单
     */
    ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO);
}
