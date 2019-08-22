package io.seata.samples.mutiple.datasource.common.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 扣减余额请求 VO
 *
 * @author HelloWoodes
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReduceBalanceRequestVO {

    private Long userId;

    private Integer price;
}
