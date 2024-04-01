package org.apache.seata.model;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class E2EConfig {
    private Inherit inherit;
    private String scene_name;
    private Retry retry;
    private Modules modules;
    private Case[] cases;

    public Case[] getCases() {
        return cases;
    }

    public void setCases(Case[] cases) {
        this.cases = cases;
    }

    public Inherit getInherit() {
        return inherit;
    }

    public void setInherit(Inherit inherit) {
        this.inherit = inherit;
    }

    public String getScene_name() {
        return scene_name;
    }

    public void setScene_name(String scene_name) {
        this.scene_name = scene_name;
    }

    public Retry getRetry() {
        return retry;
    }

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    public Modules getModules() {
        return modules;
    }

    public void setModules(Modules modules) {
        this.modules = modules;
    }
}
