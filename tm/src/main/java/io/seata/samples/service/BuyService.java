package io.seata.samples.service;

public interface BuyService {
    Boolean placeOrder(Long accountId, Long stockId, Long quantity, boolean success);

    Boolean updateOrder(Long accountId, Long stockId, Long quantity, Long orderId, boolean success);

}
