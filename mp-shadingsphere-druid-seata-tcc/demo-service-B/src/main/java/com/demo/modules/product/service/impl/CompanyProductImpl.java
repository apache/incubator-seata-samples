package com.demo.modules.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.modules.product.entity.CompanyProduct;
import com.demo.modules.product.mapper.CompanyProductMapper;
import com.demo.modules.product.service.ICompanyProductService;
import org.springframework.stereotype.Service;

@Service
public class CompanyProductImpl extends ServiceImpl<CompanyProductMapper, CompanyProduct> implements ICompanyProductService {
}
