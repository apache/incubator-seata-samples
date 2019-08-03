package io.seata.sample.controller;

import io.seata.sample.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2017/12/25
 */
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/debit")
    public Boolean debit(String userId, BigDecimal money) {
        accountService.debit(userId, money);

        return true;
    }
}
