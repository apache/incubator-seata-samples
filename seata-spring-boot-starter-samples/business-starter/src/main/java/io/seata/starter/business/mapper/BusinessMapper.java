package io.seata.starter.business.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lkx_soul
 * @create 2019-12-23-14:33
 * @Description Mysql Mapper 测试
 */
@Mapper
public interface BusinessMapper {

    /**
     * 添加库存信息
     * @param sid    账户ID
     * @param count  数量
     * @param money  价格
     * @return
     */
    @Insert("insert into seata(sid,count,money) values(#{sid},#{count},#{money})")
    int insertInfo(@Param("sid") String sid, @Param("count") int count, @Param("money") double money);
}
