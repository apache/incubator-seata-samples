package io.seata.product.service;


import io.seata.product.entity.Product;
import io.seata.product.mapper.ProductMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public void minusStock() {
        productMapper.minusStock();
    }
}
