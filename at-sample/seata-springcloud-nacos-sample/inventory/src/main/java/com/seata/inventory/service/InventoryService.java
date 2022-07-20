package com.seata.inventory.service;

public interface InventoryService {

    void deduct(String commodityCode , int count);
}