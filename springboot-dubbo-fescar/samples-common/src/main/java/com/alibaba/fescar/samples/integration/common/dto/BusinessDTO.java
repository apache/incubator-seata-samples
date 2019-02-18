package com.alibaba.fescar.samples.integration.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/14 17:26
 */
@Data
public class BusinessDTO implements Serializable {

    private String userId;

    private String commodityCode;

    private String name;

    private Integer count;

    private BigDecimal amount;
}
