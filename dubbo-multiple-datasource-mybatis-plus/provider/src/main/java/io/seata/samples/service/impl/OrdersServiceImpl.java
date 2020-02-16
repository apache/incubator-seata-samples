package io.seata.samples.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.seata.samples.entity.Orders;
import io.seata.samples.mapper.OrdersMapper;
import io.seata.samples.service.IOrdersService;

@Service(version = "1.0.0", interfaceClass = IOrdersService.class)
@DS(value = "master_2")
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

}
