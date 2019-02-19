package com.alibaba.fescar.samples.integration.account.mapper;


import com.alibaba.fescar.samples.integration.account.entity.TAccount;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface TAccountMapper extends BaseMapper<TAccount> {

    int decreaseAccount(@Param("userId") String userId, @Param("amount") Double amount);
}
