package com.seata.inventory.dao;

import com.seata.inventory.model.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;


public interface InventoryDAO extends JpaRepository<Inventory, String> {

    Inventory findByCommodityCode(String commodityCode);

}
