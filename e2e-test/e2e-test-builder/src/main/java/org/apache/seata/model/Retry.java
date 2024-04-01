package org.apache.seata.model;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class Retry {
    private int max;
    private String interval                                                                                                                                                                                                                                    ;
    private String total_timeout;

    public String getTotal_timeout() {
        return total_timeout;
    }

    public void setTotal_timeout(String total_timeout) {
        this.total_timeout = total_timeout;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
