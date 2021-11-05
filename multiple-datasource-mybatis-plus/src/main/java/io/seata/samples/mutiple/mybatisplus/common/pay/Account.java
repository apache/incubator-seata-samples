package io.seata.samples.mutiple.mybatisplus.common.pay;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @author HelloWood
 */
@Data
@Builder
public class Account {

    private Long id;

    private Integer balance;

    private Date lastUpdateTime;
}
