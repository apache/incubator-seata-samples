package com.demo.modules.product.controller;

import com.demo.modules.product.action.CompanyProductAction;
import com.seata.common.api.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "产品库存")
public class CompanyProductController {
    @Autowired
    private CompanyProductAction companyProductAction;
    @GetMapping("/deduct")
    @ApiOperation("扣减库存")
    public Result<?> deduct(@RequestParam("id") Long id){
        companyProductAction.deduct(id);
        return Result.OK();
    }
}
