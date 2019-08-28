package io.seata.samples.modules.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.seata.samples.modules.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {

}