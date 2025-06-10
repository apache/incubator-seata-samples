<#--
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

setup:
  env: compose
  # Run a httpbin container, which can return YAML data
  file: docker-compose.yaml
  timeout: ${retry.total_timeout}

cleanup:
  # always never success failure
  on: always

verify:
  # verify with retry strategy
  retry:
    # max retry count
    count: ${retry.max}
    # the interval between two attempts, e.g. 10s, 1m.
    interval: ${retry.interval}

  # when a case fails, whether to stop verifying other cases. This property defaults to true.
  fail-fast: false
  # Whether to verify cases concurrently. This property defaults to false.
  concurrency: false

  cases:
    <#if cases??>
    <#list cases as case>
    - name: ${case.name}
      query: ${case.invoke}
      expected: ${case.verify}
    </#list>
    </#if>
