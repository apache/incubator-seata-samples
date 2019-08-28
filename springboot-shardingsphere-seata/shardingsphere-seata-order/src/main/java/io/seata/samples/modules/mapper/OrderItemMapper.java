package io.seata.samples.modules.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.seata.samples.modules.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemEntity> {
	
}