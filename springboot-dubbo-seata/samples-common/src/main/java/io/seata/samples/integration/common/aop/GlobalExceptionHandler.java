/*
 *  Copyright 1999-2021 Seata.io Group.
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
package io.seata.samples.integration.common.aop;

import io.seata.samples.integration.common.enums.RspStatusEnum;
import io.seata.samples.integration.common.exception.DefaultException;
import io.seata.samples.integration.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: heshouyou
 * @Description 全局异常处理
 * @Date Created in 2019/1/9 14:12
 */

@ControllerAdvice(basePackages = "com.sinochem.finance.hsy")
@Slf4j
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ObjectResponse exceptionHandler(Exception e) {
        LOGGER.error("【系统抛出Exception异常】 —— 异常内容如下：{}", e);
        ObjectResponse objectResponse = new ObjectResponse<>();
        objectResponse.setStatus(RspStatusEnum.FAIL.getCode());
        objectResponse.setMessage(RspStatusEnum.FAIL.getMessage());
        return objectResponse;
    }

    @ExceptionHandler(DefaultException.class)
    @ResponseBody
    public ObjectResponse defaultException(DefaultException e) {
        LOGGER.error("【系统抛出SinochemException异常】 —— 异常内容如下：{}", e);
        ObjectResponse objectResponse = new ObjectResponse<>();
        objectResponse.setStatus(RspStatusEnum.FAIL.getCode());
        objectResponse.setMessage(RspStatusEnum.FAIL.getMessage());
        return objectResponse;
    }
}
