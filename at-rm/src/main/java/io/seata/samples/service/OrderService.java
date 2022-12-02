package io.seata.samples.service;

import java.math.BigDecimal;

public interface OrderService {
    Long createOrder(Long accountId, Long stockId, Long quantity);

    Boolean updateOrder(Long accountId, Long orderId, Long stockId, Long quantity);

    Integer createOrUpdateOrder(Long id, Long orderNumber, Long accountId, Long stockId, Long quantity,String note);

    Integer createOrUpdateOrder2(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note);

    Boolean addOrUpdateStock( BigDecimal quantity,BigDecimal price);

    Boolean addOrUpdateStock2(Long stockId ,BigDecimal quantity,BigDecimal price);

}
