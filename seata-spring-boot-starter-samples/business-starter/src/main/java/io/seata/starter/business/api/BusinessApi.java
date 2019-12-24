package io.seata.starter.business.api;

/**
 * @author lkx_soul
 * @create 2019-12-23-14:38
 * @Description  Seata 测试 api
 */
public interface BusinessApi {

    /**
     * 测试添加数据
     * @param count 数量
     * @param money 价格
     * @return
     */
    String insertInfo(Integer count, Double money, boolean isBack);

}
