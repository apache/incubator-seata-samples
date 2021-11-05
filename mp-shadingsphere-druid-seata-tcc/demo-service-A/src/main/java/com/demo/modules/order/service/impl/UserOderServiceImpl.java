package com.demo.modules.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.modules.order.action.UserOrderTccAction;
import com.demo.modules.order.entity.UserOrder;
import com.demo.modules.order.mapper.UserOrderMapper;
import com.demo.modules.order.service.UserOderService;
import com.seata.common.api.vo.Result;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class UserOderServiceImpl  extends ServiceImpl<UserOrderMapper, UserOrder> implements UserOderService {
    @Autowired
    private UserOrderTccAction userOrderTccAction;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    @GlobalTransactional
    @Transactional
    public void geneOrder(UserOrder userOrder) {
        // 扣减库存
        ResponseEntity<Result> forEntity = restTemplate.getForEntity("http://localhost:8071/deduct?id=1", Result.class);
        if(forEntity.getStatusCode() != HttpStatus.OK ||
                Optional.ofNullable(forEntity.getBody()).orElse(new Result()).getCode() != 200) {
            throw new RuntimeException("扣减库存失败！");
        }
        // 生成订单
        long id = IdWorker.getId();
        userOrder.setId(id);
        userOrderTccAction.geneOrder(userOrder,id);

        int a = 1/0;
    }
}
