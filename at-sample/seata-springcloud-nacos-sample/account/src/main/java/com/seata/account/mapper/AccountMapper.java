package com.seata.account.mapper;


import com.seata.account.model.Account;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository
public interface AccountMapper extends Mapper<Account> {

    /**
     * 从用户账户中借出
     */
    @Update("update account_tbl set money = money - #{money} where user_id = #{userId}")
    void debit(String userId, int money);

}
