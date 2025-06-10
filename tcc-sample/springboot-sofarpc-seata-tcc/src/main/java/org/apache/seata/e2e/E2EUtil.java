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

package org.apache.seata.e2e;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class E2EUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(E2EUtil.class);
    private E2EUtil() {
    }

    public static void writeE2EResFile(String outPutRes, String name) {
        try {
            Files.write(Paths.get(name), outPutRes.getBytes());
        } catch (IOException e) {
           LOGGER.error("write E2EResFile error", e);
        }
    }

    public static boolean isInE2ETest() {
        Map<String, String> envs = System.getenv();
        String env = envs.getOrDefault("E2E_ENV", "");
        return "open".equals(env);
    }
}
