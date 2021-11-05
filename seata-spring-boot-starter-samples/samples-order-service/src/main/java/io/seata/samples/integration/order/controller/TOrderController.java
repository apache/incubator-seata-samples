package io.seata.samples.integration.order.controller;

import io.seata.samples.integration.common.dto.OrderDTO;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.order.service.ITOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单服务
 * </p>
 *
 * * @author lidong
 *
 * @since 2019-09-04
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class TOrderController {

    @Autowired
    private ITOrderService orderService;

    @PostMapping("/create_order")
    ObjectResponse<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        log.info("请求订单微服务：{}", orderDTO.toString());
        return orderService.createOrder(orderDTO);
    }
}

