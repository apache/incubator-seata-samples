package com.alibaba.seata.sample.service;

import com.alibaba.seata.sample.entity.Storage;
import com.alibaba.seata.sample.repository.StorageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019-04-04
 */
@Service
public class StorageService {

    @Autowired
    private StorageDAO storageDAO;

    @Transactional
    public void deduct(String commodityCode, int count){
        Storage storage = storageDAO.findByCommodityCode(commodityCode);
        storage.setCount(storage.getCount()-count);

        storageDAO.save(storage);
    }
}
