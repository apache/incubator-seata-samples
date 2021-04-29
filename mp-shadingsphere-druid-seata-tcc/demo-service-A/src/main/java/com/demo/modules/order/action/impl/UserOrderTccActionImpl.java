package com.demo.modules.order.action.impl;

import com.demo.modules.order.action.UserOrderTccAction;
import com.demo.modules.order.entity.UserOrder;
import com.demo.modules.order.service.UserOderService;
import com.seata.common.api.vo.Result;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
public class UserOrderTccActionImpl implements UserOrderTccAction {
    @Autowired
    private UserOderService userOderService;

    @Override
    public void geneOrder(UserOrder userOrder, Long id) {
        // 生成订单
        userOderService.save(userOrder);
        log.info("geneOrder---------------------{}",id);
    }

    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        Long id = (Long) businessActionContext.getActionContext("id");
        log.info("commit---------------------{}",id);
        return true;
    }

    @Override
    public boolean cancel(BusinessActionContext businessActionContext) {
        Long id = (Long) businessActionContext.getActionContext("id");
        log.info("cancel---------------------{}",id);
        if(id == null) {
            return true;
        }
        userOderService.removeById(id);
        return true;
    }
}
