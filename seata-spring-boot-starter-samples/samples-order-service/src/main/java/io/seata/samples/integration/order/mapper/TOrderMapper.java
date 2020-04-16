package io.seata.samples.integration.order.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.seata.samples.integration.order.entity.TOrder;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * * @author lidong
 * @since 2019-09-04
 */
public interface TOrderMapper extends BaseMapper<TOrder> {

    /**
     * 创建订单
     * @Param:  order 订单信息
     * @Return:
     */
    void createOrder(@Param("order") TOrder order);
}
