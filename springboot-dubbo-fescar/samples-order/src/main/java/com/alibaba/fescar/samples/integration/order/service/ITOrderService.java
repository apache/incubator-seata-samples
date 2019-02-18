package com.alibaba.fescar.samples.integration.order.service;

import com.alibaba.fescar.samples.integration.common.dto.OrderDTO;
import com.alibaba.fescar.samples.integration.common.response.ObjectResponse;
import com.alibaba.fescar.samples.integration.order.entity.TOrder;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  创建订单
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface ITOrderService extends IService<TOrder> {

    /**
     * 创建订单
     */
    ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO);
}
