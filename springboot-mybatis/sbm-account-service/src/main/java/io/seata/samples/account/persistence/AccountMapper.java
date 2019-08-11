package io.seata.samples.account.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {

    @Select("select * from account_tbl where user_id =#{userId}")
    Account findByUserId(@Param("userId") String userId);

    int updateById(Account record);

}