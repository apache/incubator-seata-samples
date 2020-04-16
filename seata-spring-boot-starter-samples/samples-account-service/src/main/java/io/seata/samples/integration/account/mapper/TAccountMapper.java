package io.seata.samples.integration.account.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.seata.samples.integration.account.entity.TAccount;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * * @author lidong
 * @since 2019-09-04
 */
public interface TAccountMapper extends BaseMapper<TAccount> {

    /**
     * 减少账户余额
     * @param userId
     * @param amount
     * @return
     */
    int decreaseAccount(@Param("userId") String userId, @Param("amount") Double amount);
}
