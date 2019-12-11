package io.seata.samples.mutiple.mybatisplus.service;

/**
 * @author HelloWoodes
 */
public interface PayService {
    /**
     * @param userId 用户 ID
     * @param price  扣减金额
     * @return 返回操作结果
     * @throws Exception 失败时抛出异常
     */
    boolean reduceBalance(Long userId, Integer price) throws Exception;

}
