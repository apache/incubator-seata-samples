package io.seata.samples.mutiple.mybatisplus.common.stock;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

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
