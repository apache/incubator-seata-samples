package io.seata.samples.mutiple.datasource.controller;

import io.seata.samples.mutiple.datasource.common.OperationResponse;
import io.seata.samples.mutiple.datasource.common.order.PlaceOrderRequestVO;
import io.seata.samples.mutiple.datasource.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author HelloWoodes
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    @ResponseBody
    public OperationResponse placeOrder(@RequestBody PlaceOrderRequestVO placeOrderRequestVO) throws Exception {
        log.info("收到下单请求,用户:{}, 商品:{}", placeOrderRequestVO.getUserId(), placeOrderRequestVO.getProductId());
        return orderService.placeOrder(placeOrderRequestVO);
    }
}
