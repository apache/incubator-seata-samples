package io.seata.samples.sca.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yu.hb on 2019-10-30
 */
@RestController
public class ProviderController {


    @GetMapping("/feign/echo")
    public String feignEcho(String name) {
        return "feignEcho() hi " + name;
    }
}
