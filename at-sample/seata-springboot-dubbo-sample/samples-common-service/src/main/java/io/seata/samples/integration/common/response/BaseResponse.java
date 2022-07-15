package io.seata.samples.integration.common.response;

import java.io.Serializable;

import lombok.Data;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
