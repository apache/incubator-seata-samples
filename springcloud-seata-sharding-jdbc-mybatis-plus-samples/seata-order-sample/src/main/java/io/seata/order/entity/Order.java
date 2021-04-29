package io.seata.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

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
