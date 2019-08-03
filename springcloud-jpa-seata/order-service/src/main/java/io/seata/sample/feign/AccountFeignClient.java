package io.seata.sample.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019-04-04
 */
@FeignClient(name = "account-service", url = "127.0.0.1:8083")
public interface AccountFeignClient {

    @GetMapping("/debit")
    Boolean debit(@RequestParam("userId") String userId, @RequestParam("money") BigDecimal money);
}
