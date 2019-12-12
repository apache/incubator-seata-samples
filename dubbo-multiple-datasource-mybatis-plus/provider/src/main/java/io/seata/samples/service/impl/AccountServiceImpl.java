package io.seata.samples.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.seata.samples.entity.Account;
import io.seata.samples.mapper.AccountMapper;
import io.seata.samples.service.IAccountService;

@Service(version = "1.0.0", interfaceClass = IAccountService.class)
@DS(value = "master_1")
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

}
