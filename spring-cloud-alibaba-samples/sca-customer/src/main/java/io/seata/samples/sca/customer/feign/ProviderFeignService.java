package io.seata.samples.sca.customer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by yu.hb on 2019-10-30
 */
@FeignClient(value = "sca-provider")
public interface ProviderFeignService {
    @GetMapping("/feign/echo")
    String feignEcho(@RequestParam("name") String name);
}
