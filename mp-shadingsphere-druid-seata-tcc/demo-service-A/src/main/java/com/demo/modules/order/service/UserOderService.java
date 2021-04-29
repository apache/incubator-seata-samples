package com.demo.modules.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.modules.order.entity.UserOrder;

public interface UserOderService extends IService<UserOrder> {
    void geneOrder(UserOrder userOrder);
}
