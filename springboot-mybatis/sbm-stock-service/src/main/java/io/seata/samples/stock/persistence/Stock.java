package io.seata.samples.stock.persistence;

import lombok.Data;

@Data
public class Stock {
    private Integer id;

    private String commodityCode;

    private Integer count;

}