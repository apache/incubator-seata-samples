package io.seata.starter.samples.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author lkx_soul
 * @create 2019-12-23-14:33
 * @Description Mysql Mapper 测试
 */
@Mapper
public interface AccountMapper {

    /**
     * 修改库存
     * @param sid    商品ID
     * @param count  数量
     * @return
     */
    @Insert("update seata set count=count-#{count} where sid = #{sid}")
    int updateAccount(@Param("sid") String sid, @Param("count") int count);

    @Select("select count from seata where sid = #{sid}")
    int selectCount(@Param("sid") String sid);
}
