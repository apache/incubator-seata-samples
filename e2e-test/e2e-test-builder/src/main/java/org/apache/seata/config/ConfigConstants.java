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

import java.util.HashMap;
import java.util.Map;

/**
 * @author jingliu_xiong@foxmail.com
 */
public class ConfigConstants {
    public static final String IMAGE_DIR = "tmp/images";
    public static final String SCENE_DIR = "tmp/scene-test";
    public static final String REPLACE_DIR = "tmp/replace";
    public static final String SEATA_E2E_FILE = "seata-e2e.yaml";
    public static final String E2E_FILES = "e2e-files";
    public static final String COMPOSE_FILE = "docker-compose.yaml";
    public static final String SKY_WALKING_E2E_FILE = "e2e.yaml";
    public static final String IMAGE_VERSION = "0.0.1";
    public static final String DOCKER_SERVICE_JAR_TYPE = "Jar";
    public static final String DOCKER_SERVICE_APPLICATION_TYPE = "Application";
    
    // JDK base image configuration
    public static final String DEFAULT_BASE_IMAGE = "openjdk:8-jdk-alpine";
    public static final String ENV_JDK_BASE_IMAGE = "E2E_JDK_BASE_IMAGE";
    
    // Predefined JDK image mapping
    private static final Map<String, String> JDK_IMAGE_MAP = new HashMap<String, String>() {{
        put("8", "eclipse-temurin:8-jdk-alpine");
        put("11", "eclipse-temurin:8-jdk-alpine");
        put("17", "eclipse-temurin:17-jdk-alpine");
        put("21", "eclipse-temurin:21-jdk-alpine");
        // Support for different distributions
        put("8-zulu", "azul/zulu-openjdk-alpine:8");
        put("11-zulu", "azul/zulu-openjdk-alpine:11");
        put("17-zulu", "azul/zulu-openjdk-alpine:17");
        put("21-zulu", "azul/zulu-openjdk-alpine:21");
    }};
    
    /**
     * Get base image for Docker build
     * Priority: Environment Variable > Default Value
     * 
     * @return base image string
     */
    public static String getBaseImage() {
        String envImage = System.getenv(ENV_JDK_BASE_IMAGE);
        if (envImage != null && !envImage.isEmpty()) {
            // If it's a full image name (contains : or /), use it directly
            if (envImage.contains(":") || envImage.contains("/")) {
                System.out.println("[E2E] Using custom base image from environment: " + envImage);
                return envImage;
            }
            // Otherwise, lookup from the predefined mapping
            String mappedImage = JDK_IMAGE_MAP.get(envImage);
            if (mappedImage != null) {
                System.out.println("[E2E] Using mapped base image for JDK " + envImage + ": " + mappedImage);
                return mappedImage;
            }
            System.out.println("[E2E] Warning: Unknown JDK version '" + envImage + "', using default: " + DEFAULT_BASE_IMAGE);
        } else {
            System.out.println("[E2E] Using default base image: " + DEFAULT_BASE_IMAGE);
        }
        return DEFAULT_BASE_IMAGE;
    }
}
