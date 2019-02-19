package com.alibaba.fescar.samples.integration.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: heshouyou
 * @Description  账户信息
 * @Date Created in 2019/1/13 16:39
 */
@Data
public class AccountDTO implements Serializable {

    private Integer id;

    private String userId;

    private BigDecimal amount;
}
