package com.seata.inventory.service;

public interface InventoryService {

    void occupy(String commodityCode , int count);

    void rollBackInventory(String commodityCode, int count);
}