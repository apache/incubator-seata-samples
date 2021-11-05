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
package io.seata.samples.service;

import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * @author 陈健斌
 * @date 2019/12/05
 */
public interface DemoService {

    /**
     * @return
     * @throws TransactionException
     */
    public Object testRollback() throws TransactionException;

    /**
     * @return
     * @throws TransactionException
     */
    @GlobalTransactional
    public Object testCommit() throws TransactionException;
}
