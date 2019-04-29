package io.seata.sample.controller;

import io.seata.sample.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019/3/28
 */
@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping(path = "/deduct")
    public Boolean deduct(String commodityCode, Integer count) {
        storageService.deduct(commodityCode, count);
        return true;
    }
}
