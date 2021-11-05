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
package com.seata.starter.core.handler;

import com.seata.common.api.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@ConditionalOnClass(ViewResolver.class)
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> MethodArgumentNotValidException(Exception ex, HttpServletRequest request,
                                                     HttpServletResponse response) {
        ex.printStackTrace();
        MethodArgumentNotValidException c = (MethodArgumentNotValidException)ex;
        List<ObjectError> errors = c.getBindingResult().getAllErrors();
        //        StringBuffer errorMsg=new StringBuffer();
        //        errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
        return Result.error(errors.get(0).getDefaultMessage());
    }

    // 默认的异常
    @ExceptionHandler(value = Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}
