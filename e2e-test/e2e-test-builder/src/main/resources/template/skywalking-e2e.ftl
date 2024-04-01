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
