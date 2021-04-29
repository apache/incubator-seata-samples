package com.demo.modules.product.action.impl;

import com.demo.modules.product.action.CompanyProductAction;
import com.demo.modules.product.entity.CompanyProduct;
import com.demo.modules.product.service.ICompanyProductService;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@Slf4j
public class CompanyProductActionImpl implements CompanyProductAction {
    @Autowired
    private ICompanyProductService companyProductService;
    @Override
    @Transactional
    public void deduct(Long id) {
        CompanyProduct id1 = companyProductService.query().eq("id", id).one();
        id1.setAccount(id1.getAccount()-1);
        companyProductService.updateById(id1);
    }

    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        Long id = Long.parseLong(businessActionContext.getActionContext("id")+"") ;
        log.info("cancel---------------------{}",id);
        return true;
    }

    @Override
    public boolean cancel(BusinessActionContext businessActionContext) {
        Long id = Long.parseLong(businessActionContext.getActionContext("id")+"") ;
        log.info("cancel---------------------{}",id);
        CompanyProduct id1 = companyProductService.query().eq("id", id).one();
        id1.setAccount(id1.getAccount()+1);
        companyProductService.updateById(id1);
        return true;
    }
}
