package io.seata.sample.entity;


import java.math.BigDecimal;
import lombok.Data;

/**
 * 订单
 * @author IT云清
 */
@Data
public class Order {

    private Long id;

    private Long userId;

    private Long productId;

    private Integer count;

    private BigDecimal money;

    /**订单状态：0：创建中；1：已完结*/
    private Integer status;

}
