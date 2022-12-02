/*
 *  Copyright 1999-2022 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
