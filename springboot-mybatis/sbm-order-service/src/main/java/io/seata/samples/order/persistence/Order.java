package io.seata.samples.order.persistence;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    private Integer id;

    private String userId;

    private String commodityCode;

    private Integer count;

    private BigDecimal money;

}