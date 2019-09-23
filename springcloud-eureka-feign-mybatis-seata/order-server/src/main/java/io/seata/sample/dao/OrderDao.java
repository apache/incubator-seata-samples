package io.seata.sample.dao;

import io.seata.sample.entity.Order;
import org.springframework.stereotype.Repository;

/**
 * @author IT云清
 */
@Repository
public interface OrderDao {

    /**
     * 创建订单
     * @param order
     * @return
     */
    void create(Order order);
}
