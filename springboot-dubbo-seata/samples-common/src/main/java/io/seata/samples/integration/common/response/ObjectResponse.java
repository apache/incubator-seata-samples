package io.seata.samples.integration.common.response;

import java.io.Serializable;

/**
 * @author: heshouyou
 * @date: 2018-07-03 16:55
 */
public class ObjectResponse<T> extends BaseResponse implements Serializable {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
