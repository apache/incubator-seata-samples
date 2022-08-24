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
package io.seata.samples.tcc.dubbo.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Result holder.
 *
 * @author zhangsen
 */
public class ResultHolder {

    private static Map<String, String> actionOneResults = new ConcurrentHashMap<String, String>();

    private static Map<String, String> actionTwoResults = new ConcurrentHashMap<String, String>();

    /**
     * Set action one result.
     *
     * @param txId   the tx id
     * @param result the result
     */
    public static void setActionOneResult(String txId, String result) {
        actionOneResults.put(txId, result);
    }

    /**
     * Get action one result string.
     *
     * @param txId the tx id
     * @return the string
     */
    public static String getActionOneResult(String txId) {
        return actionOneResults.get(txId);
    }

    /**
     * Set action two result.
     *
     * @param txId   the tx id
     * @param result the result
     */
    public static void setActionTwoResult(String txId, String result) {
        actionTwoResults.put(txId, result);
    }

    /**
     * Get action two result string.
     *
     * @param txId the tx id
     * @return the string
     */
    public static String getActionTwoResult(String txId) {
        return actionTwoResults.get(txId);
    }

}
