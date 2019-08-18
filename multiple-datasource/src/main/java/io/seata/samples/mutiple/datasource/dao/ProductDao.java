package io.seata.samples.mutiple.datasource.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author HelloWoodes
 */
@Mapper
public interface ProductDao {

    /**
     * 获取库存
     *
     * @param productId 商品 ID
     * @return 库存
     */
    @Select("SELECT stock FROM product WHERE id = #{productId}")
    Integer getStock(@Param("productId") Long productId);


    /**
     * 扣减库存
     *
     * @param productId 商品 ID
     * @param amount    扣减数量
     * @return 影响记录行数
     */
    @Update("UPDATE product SET stock = stock - #{amount} WHERE id = #{productId}")
    Integer reduceStock(@Param("productId") Long productId, @Param("amount") Integer amount);
}
