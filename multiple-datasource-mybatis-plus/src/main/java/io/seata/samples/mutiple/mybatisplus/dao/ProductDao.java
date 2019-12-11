package io.seata.samples.mutiple.mybatisplus.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.seata.samples.mutiple.mybatisplus.common.storage.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HelloWood
 */
@Mapper
public interface ProductDao extends BaseMapper<Product> {

}
