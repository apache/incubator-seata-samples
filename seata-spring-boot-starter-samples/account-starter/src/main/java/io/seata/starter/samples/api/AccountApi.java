package io.seata.starter.samples.api;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lkx_soul
 * @create 2019-12-23-14:38
 * @Description  Seata 测试 api
 */
public interface AccountApi {

    /**
     * 减少库存
     * @param sid   商品ID
     * @param count 数量
     * @return
     */
    boolean updateAccount(String sid, int count);

}
