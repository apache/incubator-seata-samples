package io.seata.sample.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-xa", url = "127.0.0.1:8083")
public interface AccountFeignClient {
    @GetMapping("/reduce")
    String reduce(@RequestParam("userId") String userId, @RequestParam("money") int money);
}
