package com.seata.order.dao;


import com.seata.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderDAO extends JpaRepository<Order, Long> {

}
