package io.seata.samples.mutiple.mybatisplus.common.stock;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author HelloWood
 */
@Data
@Builder
public class Product {

    private Integer id;

    private Double price;

    private Integer stock;

    private Date lastUpdateTime;
}
