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
