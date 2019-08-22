package io.seata.samples.mutiple.datasource.service.impl;

import io.seata.core.context.RootContext;
import io.seata.samples.mutiple.datasource.config.DataSourceKey;
import io.seata.samples.mutiple.datasource.config.DynamicDataSourceContextHolder;
import io.seata.samples.mutiple.datasource.dao.ProductDao;
import io.seata.samples.mutiple.datasource.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HelloWoodes
 */
@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    @Autowired
    private ProductDao productDao;

    /**
     * 事务传播特性设置为 REQUIRES_NEW 开启新的事务
     *
     * @param productId 商品 ID
     * @param amount    扣减数量
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean reduceStock(Long productId, Integer amount) throws Exception {
        log.info("=============STORAGE=================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.STORAGE);
        log.info("当前 XID: {}", RootContext.getXID());

        // 检查库存
        checkStock(productId, amount);

        log.info("开始扣减 {} 库存", productId);
        // 扣减库存
        Integer record = productDao.reduceStock(productId, amount);
        log.info("扣减 {} 库存结果:{}", productId, record > 0 ? "操作成功" : "扣减库存失败");

        return record > 0;

    }

    private void checkStock(Long productId, Integer requiredAmount) throws Exception {

        log.info("检查 {} 库存", productId);
        Integer stock = productDao.getStock(productId);

        if (stock < requiredAmount) {
            log.warn("{} 库存不足，当前库存:{}", productId, stock);
            throw new Exception("库存不足");
        }
    }
}
