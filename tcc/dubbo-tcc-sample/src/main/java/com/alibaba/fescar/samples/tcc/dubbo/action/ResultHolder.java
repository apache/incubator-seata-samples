package com.alibaba.fescar.samples.tcc.dubbo.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangsen
 */
public class ResultHolder {

    private static Map<String, String> actionOneResults = new ConcurrentHashMap<String, String>();

    private static Map<String, String> actionTwoResults = new ConcurrentHashMap<String, String>();

    public static void setActionOneResult(String txId, String result){
        actionOneResults.put(txId, result);
    }

    public static String getActionOneResult(String txId){
        return actionOneResults.get(txId);
    }

    public static void setActionTwoResult(String txId, String result){
        actionTwoResults.put(txId, result);
    }

    public static String getActionTwoResult(String txId){
        return actionTwoResults.get(txId);
    }

}
