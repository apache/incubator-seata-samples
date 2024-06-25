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
package org.apache.seata.config;

import org.apache.seata.model.E2EConfig;
import org.apache.seata.model.E2EWrapper;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class ConfigReader {
    public static E2EConfig readConfig(File configFile) throws IOException {
        Yaml yaml = new Yaml(new Constructor(E2EWrapper.class));
        E2EWrapper e2EWrapper = new E2EWrapper();
        InputStream inputStream = Files.newInputStream(configFile.toPath());
        // 从YAML文件读取数据并转换为Java对象
        e2EWrapper = yaml.load(inputStream);
        return e2EWrapper.getE2e();
    }
}
