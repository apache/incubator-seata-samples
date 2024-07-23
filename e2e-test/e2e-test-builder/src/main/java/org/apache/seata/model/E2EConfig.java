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

/**
 * @author jingliu_xiong@foxmail.com
 */
public class E2EConfig {
    private Inherit inherit;
    private String scene_name;
    private Retry retry;
    private Modules modules;
    private Case[] cases;
    private Replace[] replace;

    public Replace[] getReplace() {
        return replace;
    }

    public void setReplace(Replace[] replace) {
        this.replace = replace;
    }

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
