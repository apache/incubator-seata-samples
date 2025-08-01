/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.apache.dubbo.samples.seata.business;

import org.apache.dubbo.samples.seata.api.BusinessService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BusinessApplicationIT.class)
public class BusinessApplicationIT {

    @Autowired
    private BusinessService businessService;

    @Test
    void testRollback() {
        Assertions.assertThrows(RuntimeException.class, () -> businessService.purchaseRollback("ACC_001", "STOCK_001", 1));
    }

    @Test
    void testCommit() {
        Assertions.assertDoesNotThrow(() -> businessService.purchaseCommit("ACC_001", "STOCK_001", 1));
    }

}
