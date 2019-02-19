package com.alibaba.fescar.samples.integration.order.dubbo;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.samples.integration.common.dto.OrderDTO;
import com.alibaba.fescar.samples.integration.common.dubbo.OrderDubboService;
import com.alibaba.fescar.samples.integration.common.response.ObjectResponse;
import com.alibaba.fescar.samples.integration.order.service.ITOrderService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/23 15:59
 */
@Service(version = "1.0.0",protocol = "${dubbo.protocol.id}",
        application = "${dubbo.application.id}",registry = "${dubbo.registry.id}",
        timeout = 3000)
public class OrderDubboServiceImpl implements OrderDubboService {

    @Autowired
    private ITOrderService orderService;

    @Override
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO) {
        System.out.println("全局事务id ：" + RootContext.getXID());
        return orderService.createOrder(orderDTO);
    }
}
