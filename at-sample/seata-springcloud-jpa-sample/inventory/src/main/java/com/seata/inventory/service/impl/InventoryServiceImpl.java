package com.seata.inventory.service.impl;

import com.seata.inventory.dao.InventoryDAO;
import com.seata.inventory.model.Inventory;
import com.seata.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryDAO inventoryDAO;


    @Override
    public void deduct(String commodityCode, int count) {
        Inventory inventory = inventoryDAO.findByCommodityCode(commodityCode);
        inventory.setCount(inventory.getCount() - count);
        inventoryDAO.save(inventory);

    }
}
