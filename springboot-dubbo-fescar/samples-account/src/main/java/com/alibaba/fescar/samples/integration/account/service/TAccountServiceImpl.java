package com.alibaba.fescar.samples.integration.account.service;

import com.alibaba.fescar.samples.integration.account.entity.TAccount;
import com.alibaba.fescar.samples.integration.account.mapper.TAccountMapper;
import com.alibaba.fescar.samples.integration.common.dto.AccountDTO;
import com.alibaba.fescar.samples.integration.common.enums.RspStatusEnum;
import com.alibaba.fescar.samples.integration.common.response.ObjectResponse;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
@Service
public class TAccountServiceImpl extends ServiceImpl<TAccountMapper, TAccount> implements ITAccountService {

    @Override
    public ObjectResponse decreaseAccount(AccountDTO accountDTO) {
        int account = baseMapper.decreaseAccount(accountDTO.getUserId(), accountDTO.getAmount().doubleValue());
        ObjectResponse<Object> response = new ObjectResponse<>();
        if (account > 0){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    }
}
