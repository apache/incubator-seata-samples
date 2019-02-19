package com.alibaba.fescar.samples.integration.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 基本返回
 *
 * @author: heshouyou
 * @date: 2018-07-03 16:46
 */
@Data
public class BaseResponse implements Serializable {

    private int status = 200;

    private String message;

}
