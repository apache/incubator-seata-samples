package com.alibaba.fescar.samples.integration.common.enums;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/14 10:18
 */
public enum  RspStatusEnum {
    SUCCESS(200,"成功"),
    FAIL(999,"失败"),
    EXCEPTION(500,"系统异常");

    private int code;

    private String message;

    RspStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
