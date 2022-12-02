package io.seata.samples.service;

import java.math.BigDecimal;

public interface BuyService {
    Boolean placeOrder(Long accountId, Long stockId, Long quantity, boolean success);

    Boolean updateOrder(Long accountId, Long stockId, Long quantity, Long orderId, boolean success);

    Integer createOrUpdateOrder(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note, boolean success);

    Boolean createOrUpdateOrder2(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note, boolean success);

    Boolean addOrUpdateStock(BigDecimal quantity, BigDecimal price, boolean success);

    Boolean addOrUpdateStock2(Long stockId, BigDecimal quantity, BigDecimal price, boolean success);
}
