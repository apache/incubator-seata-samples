package io.seata.samples.service;

public interface OrderService {
    Long createOrder(Long accountId, Long stockId, Long quantity);

    Boolean updateOrder(Long accountId, Long orderId, Long stockId, Long quantity);
}
