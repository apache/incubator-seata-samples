package com.demo.modules.order.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.demo.modules.order.action.UserOrderTccAction;
import com.demo.modules.order.entity.UserOrder;
import com.demo.modules.order.service.UserOderService;
import com.seata.common.api.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户订单")
public class UserOrderController {
    @Autowired
    private UserOderService userOderService;
    @GetMapping("/geneOrder")
    @ApiOperation("生成订单")
    public Result<?> geneOrder(){
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderId(IdWorker.getId());
        userOrder.setPId(1L);
        userOderService.geneOrder(userOrder);
        return Result.OK(userOrder);
    }
}
