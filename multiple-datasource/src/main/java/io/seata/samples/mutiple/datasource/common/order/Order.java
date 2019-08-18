package io.seata.samples.mutiple.datasource.common.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author HelloWoodes
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Integer id;

    private Long userId;

    private Long productId;

    private OrderStatus status;

    private Integer payAmount;
}
