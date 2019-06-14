package io.seata.sample.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/06/14
 */
@FeignClient(name = "storage-service", url = "127.0.0.1:8081")
public interface StorageFeignClient {

    @GetMapping("/deduct")
    void deduct(@RequestParam("commodityCode") String commodityCode,
                @RequestParam("count") Integer count);

}
