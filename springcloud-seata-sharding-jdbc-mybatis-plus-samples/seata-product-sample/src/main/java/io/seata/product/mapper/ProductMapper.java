package io.seata.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.seata.product.entity.Product;
import org.apache.ibatis.annotations.Update;

public interface ProductMapper extends BaseMapper<Product> {

    @Update("update product_info set stock = stock-1")
    void minusStock();
}
