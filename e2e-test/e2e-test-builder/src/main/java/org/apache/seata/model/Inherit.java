package org.apache.seata.model;

import java.util.Map;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class Inherit {
    private String name;
    private Map<String, String> props;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }
}
