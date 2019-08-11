package io.seata.samples.storage.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StorageMapper {

    @ResultType(value = Storage.class)
    @Select("select id, commodity_code commodityCode, count from storage_tbl where id =#{id}")
    Storage selectById(@Param("id") Integer id);

    @ResultType(value = Storage.class)
    @Select("select id, commodity_code commodityCode, count from storage_tbl where commodity_code =#{commodityCode}")
    Storage findByCommodityCode(@Param("commodityCode") String commodityCode);

    int updateById(Storage record);

    void insert(Storage record);

    void insertBatch(List<Storage> records);

    int updateBatch(@Param("list") List<Long> ids, @Param("commodityCode") String commodityCode);
}