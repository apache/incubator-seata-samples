package io.seata.samples.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.seata.samples.entity.Product;
import io.seata.samples.mapper.ProductMapper;
import io.seata.samples.service.IProductService;

@Service(version = "1.0.0", interfaceClass = IProductService.class)
@DS(value = "master_3")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
