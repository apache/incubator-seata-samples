<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->

# Apache Seata(incubating) Samples

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Seata](https://img.shields.io/badge/Seata-2.x-orange.svg)](https://seata.apache.org/)
[![Java](https://img.shields.io/badge/Java-8%2B-green.svg)](https://www.oracle.com/java/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://docs.docker.com/compose/)
[![GitHub Stars](https://img.shields.io/github/stars/apache/incubator-seata?style=social)](https://github.com/apache/incubator-seata/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/apache/incubator-seata?style=social)](https://github.com/apache/incubator-seata/network/members)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/apache/incubator-seata-samples/pulls)

This repository contains sample projects demonstrating how to use [Apache Seata](https://seata.apache.org/) distributed transaction solutions.

## Transaction Modes

Seata supports four transaction modes. Learn more at the [Quick Start Guide](https://seata.apache.org/docs/user/quickstart/).

| Mode | Directory | Description |
|------|-----------|-------------|
| AT | `at-sample/` | Automatic Transaction mode (recommended for most cases) |
| TCC | `tcc-sample/` | Try-Confirm-Cancel mode |
| Saga | `saga-sample/` | Long-running transaction mode |
| XA | `xa-sample/` | XA distributed transaction mode |

## Prerequisites

- JDK 8 or later
- Maven 3.6+
- Docker & Docker Compose (for local development)

## Quick Start (Local Development)

We provide `docker-compose.yaml` files to help you quickly set up the required infrastructure.

### AT Mode Samples
```bash
cd at-sample
docker-compose up -d
```
This starts MySQL (with tables initialized), Zookeeper, and Seata Server. You can then run any AT sub-sample in your IDE.

> **Note:** The `seata-e2e.yaml` files are for CI/CD integration tests. For local development, use `docker-compose.yaml`.

After infrastructure is up, start the Java applications in your IDE. Refer to each sample's documentation for specific startup order.

## Directory Structure

```
seata-samples/
├── at-sample/           # AT mode samples
│   ├── springboot-dubbo-seata/
│   ├── spring-dubbo-seata/
│   └── ...
├── tcc-sample/          # TCC mode samples
├── saga-sample/         # Saga mode samples
├── saga-annotation-sample/ # Saga annotation mode samples
├── xa-sample/           # XA mode samples
└── e2e-test/            # E2E testing framework
```

## For Contributors

- **Naming**: Use framework combination naming, e.g., `spring-nacos-seata`, `springboot-nacos-zk-seata`
- **Dependency**: Each sample should have independent dependencies and should NOT depend on the parent pom