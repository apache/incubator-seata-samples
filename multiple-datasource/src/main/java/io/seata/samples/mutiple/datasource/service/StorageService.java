package io.seata.samples.mutiple.datasource.service;

/**
 * @author HelloWoodes
 */
public interface StorageService {
    /**
     * 扣减库存
     *
     * @param productId 商品 ID
     * @param amount    扣减数量
     * @return 操作结果
     * @throws Exception 扣减失败时抛出异常
     */
    boolean reduceStock(Long productId, Integer amount) throws Exception;
}
