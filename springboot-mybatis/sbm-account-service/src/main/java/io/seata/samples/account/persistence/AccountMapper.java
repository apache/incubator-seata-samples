package io.seata.samples.account.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {

    Account selectByUserId(@Param("userId") String userId);

    int updateById(Account record);

}