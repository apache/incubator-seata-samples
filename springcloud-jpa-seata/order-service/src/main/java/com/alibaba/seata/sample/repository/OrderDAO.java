package com.alibaba.seata.sample.repository;

import com.alibaba.seata.sample.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019-04-04
 */
public interface OrderDAO extends JpaRepository<Order,Long> {

}
