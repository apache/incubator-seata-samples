package io.seata.samples.mutiple.mybatisplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.seata.samples.mutiple.mybatisplus.common.order.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HelloWood
 */
@Mapper
public interface OrderDao extends BaseMapper<Order> {

}
