package com.alibaba.fescar.samples.integration.account.service;

import com.alibaba.fescar.samples.integration.account.entity.TAccount;
import com.alibaba.fescar.samples.integration.common.dto.AccountDTO;
import com.alibaba.fescar.samples.integration.common.response.ObjectResponse;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface ITAccountService extends IService<TAccount> {

    /**
     * 扣用户钱
     */
    ObjectResponse decreaseAccount(AccountDTO accountDTO);
}
