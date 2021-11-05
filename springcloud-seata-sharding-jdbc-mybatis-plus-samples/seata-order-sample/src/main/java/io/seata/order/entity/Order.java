package io.seata.order.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("order_info")
public class Order {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String orderName;

    private Long productId;

    private Integer buyNum;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
