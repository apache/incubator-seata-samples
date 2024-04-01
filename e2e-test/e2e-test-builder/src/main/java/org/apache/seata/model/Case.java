package org.apache.seata.model;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class Case {
    private String name;
    private String invoke;
    private String verify;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvoke() {
        return invoke;
    }

    public void setInvoke(String invoke) {
        this.invoke = invoke;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
