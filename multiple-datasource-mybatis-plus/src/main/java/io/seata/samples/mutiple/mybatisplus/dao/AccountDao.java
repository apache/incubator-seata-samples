package io.seata.samples.mutiple.mybatisplus.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.seata.samples.mutiple.mybatisplus.common.pay.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HelloWood
 */
@Mapper
public interface AccountDao extends BaseMapper<Account> {

}