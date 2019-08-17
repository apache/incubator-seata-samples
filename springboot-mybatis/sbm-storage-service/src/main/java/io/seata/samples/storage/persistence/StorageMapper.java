package io.seata.samples.storage.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StorageMapper {

    Storage selectById(@Param("id") Integer id);

    Storage findByCommodityCode(@Param("commodityCode") String commodityCode);

    int updateById(Storage record);

    void insert(Storage record);

    void insertBatch(List<Storage> records);

    int updateBatch(@Param("list") List<Long> ids, @Param("commodityCode") String commodityCode);
}