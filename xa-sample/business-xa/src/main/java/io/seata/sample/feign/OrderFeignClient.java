package io.seata.sample.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-xa", url = "127.0.0.1:8082")
public interface OrderFeignClient {

    @GetMapping("/create")
    String create(@RequestParam("userId") String userId, @RequestParam("commodityCode") String commodityCode,
                  @RequestParam("orderCount") int orderCount);

}
