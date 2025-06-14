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
package org.apache.seata.action;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Result holder.
 *
 * @author zhangsen
 */
public class ResultHolder {

    private static final Map<String, CompletableFuture<Void>> actionTwoResults = new ConcurrentHashMap<>();


    /**
     * Set action two result.
     *
     * @param txId   the tx id
     * @param completableFuture the completableFuture
     */
    public static void setActionTwoResult(String txId, CompletableFuture<Void> completableFuture) {
        actionTwoResults.put(txId, completableFuture);
    }

    /**
     * Get action two result string.
     *
     * @param txId the tx id
     * @return the string
     */
    public static CompletableFuture<Void> getActionTwoResult(String txId) {
        return actionTwoResults.get(txId);
    }

}
