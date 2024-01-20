/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.seata.api.service;

import java.sql.SQLException;

/**
 * The interface Stock service.
 *
 * @author jimin.jm @alibaba-inc.com
 * @date 2019 /08/21
 */
public interface StorageService extends DataResetService {
    /**
     * Deduct.
     *
     * @param commodityCode the commodity code
     * @param count         the count
     * @throws SQLException the sql exception
     */
    void deduct(String commodityCode, int count) throws SQLException;
}