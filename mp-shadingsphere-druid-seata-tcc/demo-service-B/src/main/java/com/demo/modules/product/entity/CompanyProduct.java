package com.demo.modules.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
public class CompanyProduct implements Serializable {
    @TableId
    private Long id;
    @TableField(value = "product_name")
    private String productName;
    @TableField(value = "account")
    private Long account;
}
