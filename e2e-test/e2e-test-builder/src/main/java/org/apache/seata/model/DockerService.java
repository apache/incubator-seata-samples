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
package org.apache.seata.model;

import java.util.List;
import java.util.Map;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class DockerService {
    private String name;
    private String image;
    private String networks;
    private String network_mode;
    private String hostname;
    private String restart;
    private List<String> ports;
    private Map<String, String> healthcheck;
    private Map<String, DependOn> depends_on;
    private Map<String, String> environment;
    private List<String> volumes;
    private String container_name;

    public String getContainer_name() {
        return container_name;
    }

    public void setContainer_name(String container_name) {
        this.container_name = container_name;
    }

    public String getNetwork_mode() {
        return network_mode;
    }

    public void setNetwork_mode(String network_mode) {
        this.network_mode = network_mode;
    }

    public String getNetworks() {
        return networks;
    }

    public void setNetworks(String networks) {
        this.networks = networks;
    }

    public String getRestart() {
        return restart;
    }

    public void setRestart(String restart) {
        this.restart = restart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<String> getPorts() {
        return ports;
    }

    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    public Map<String, String> getHealthcheck() {
        return healthcheck;
    }

    public void setHealthcheck(Map<String, String> healthcheck) {
        this.healthcheck = healthcheck;
    }

    public Map<String, DependOn> getDepends_on() {
        return depends_on;
    }

    public void setDepends_on(Map<String, DependOn> depends_on) {
        this.depends_on = depends_on;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    public List<String> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<String> volumes) {
        this.volumes = volumes;
    }
}
