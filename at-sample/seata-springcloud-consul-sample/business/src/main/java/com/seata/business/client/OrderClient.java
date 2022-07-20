package com.seata.business.client;

import com.seata.business.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(name = "order-service")
@Component
public interface OrderClient {

    @PostMapping("order/create")
    public Boolean create(@RequestBody Order order);

}
