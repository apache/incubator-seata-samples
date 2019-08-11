package io.seata.samples.storage.persistence;

import lombok.Data;

@Data
public class Storage {
    private Integer id;

    private String commodityCode;

    private Integer count;

}