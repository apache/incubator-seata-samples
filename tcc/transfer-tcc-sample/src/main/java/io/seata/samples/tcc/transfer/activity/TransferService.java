package io.seata.samples.tcc.transfer.activity;

/**
 * 转账服务
 *
 * @author zhangsen
 */
public interface TransferService {

    /**
     * 转账操作
     * @param from  扣钱账户
     * @param to  加钱账户
     * @param amount  转账金额
     * @return
     */
    public boolean transfer(String from, String to, double amount);


}
