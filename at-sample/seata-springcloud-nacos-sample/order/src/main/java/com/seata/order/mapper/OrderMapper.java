package com.seata.order.mapper;


import com.seata.order.model.Order;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository
public interface OrderMapper extends Mapper<Order> {

}

