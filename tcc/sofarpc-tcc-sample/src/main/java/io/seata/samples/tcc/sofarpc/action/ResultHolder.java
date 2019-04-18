package io.seata.samples.tcc.sofarpc.action;

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
    public static void setActionOneResult(String txId, String result){
        actionOneResults.put(txId, result);
    }

    /**
     * Get action one result string.
     *
     * @param txId the tx id
     * @return the string
     */
    public static String getActionOneResult(String txId){
        return actionOneResults.get(txId);
    }

    /**
     * Set action two result.
     *
     * @param txId   the tx id
     * @param result the result
     */
    public static void setActionTwoResult(String txId, String result){
        actionTwoResults.put(txId, result);
    }

    /**
     * Get action two result string.
     *
     * @param txId the tx id
     * @return the string
     */
    public static String getActionTwoResult(String txId){
        return actionTwoResults.get(txId);
    }


}
