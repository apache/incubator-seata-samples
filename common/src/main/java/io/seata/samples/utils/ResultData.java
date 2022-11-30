package io.seata.samples.utils;

import java.io.Serializable;


public class ResultData<T> implements Serializable {
    private int status;
    public T data;
    public String msg;

    public ResultData() {
    }

    public ResultData(int status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public ResultData<T> setStatus(int status) {
        this.status = status;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultData<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultData<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public String toString() {
        return "ResultData{" +
            "status=" + status +
            ", data=" + data +
            ", msg='" + msg + '\'' +
            '}';
    }
}
