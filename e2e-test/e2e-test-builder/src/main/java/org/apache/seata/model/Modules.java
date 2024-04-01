package org.apache.seata.model;

import java.util.List;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class Modules {
    private List<Module> consumers;
    private List<Module> providers;
    private List<Module> infrastructures;

    public List<Module> getInfrastructures() {
        return infrastructures;
    }

    public void setInfrastructures(List<Module> infrastructures) {
        this.infrastructures = infrastructures;
    }

    public List<Module> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<Module> consumers) {
        this.consumers = consumers;
    }

    public List<Module> getProviders() {
        return providers;
    }

    public void setProviders(List<Module> providers) {
        this.providers = providers;
    }
}
