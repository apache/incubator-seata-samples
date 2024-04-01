package org.apache.seata.model;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class DependOn {
    private String restart;
    private String condition;
    private String required;

    public String getRestart() {
        return restart;
    }

    public void setRestart(String restart) {
        this.restart = restart;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }
}
