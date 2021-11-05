package io.seata.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("product-server")
public interface ProductClient {

    @PutMapping("/minus/stock")
    public Void minusStock();

}
