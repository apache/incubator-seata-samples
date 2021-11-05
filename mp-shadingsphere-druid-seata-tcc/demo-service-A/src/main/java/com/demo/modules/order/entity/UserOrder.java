package com.demo.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserOrder implements Serializable {
    @TableId
    private Long id;
    private Long orderId;
    private Long pId;
}
