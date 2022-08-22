package com.seata.inventory.service.impl;

import com.seata.inventory.mapper.InventoryMapper;
import com.seata.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;


    @Override
    public void occupy(String commodityCode, int count) {
        inventoryMapper.occupy(commodityCode,count);
    }

    @Override
    public void rollBackInventory(String commodityCode, int count) {
        inventoryMapper.rollBackInventory(commodityCode,count);
    }
}
