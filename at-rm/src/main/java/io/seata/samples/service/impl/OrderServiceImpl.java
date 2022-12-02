package io.seata.samples.service.impl;

import java.math.BigDecimal;

import io.seata.core.context.RootContext;
import io.seata.samples.bean.Order;
import io.seata.samples.bean.Stock;
import io.seata.samples.mapper.OrderMapper;
import io.seata.samples.mapper.StockMapper;
import io.seata.samples.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final StockMapper stockMapper;

    public OrderServiceImpl(OrderMapper orderMapper, StockMapper stockMapper) {
        this.orderMapper = orderMapper;
        this.stockMapper = stockMapper;
    }

    @Transactional
    @Override
    public Long createOrder(Long accountId, Long stockId, Long quantity) {
        Example example = new Example(Stock.class);
        example.setForUpdate(true);
        example.createCriteria().andEqualTo("id", stockId);
        Stock stock = this.stockMapper.selectOneByExample(example);
        if (stock == null) {
            throw new RuntimeException("stock can't be found,please check stock id");
        }
        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("insufficient stock quantity");
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        this.stockMapper.updateByPrimaryKeySelective(stock);
        BigDecimal amount = stock.getPrice().multiply(BigDecimal.valueOf(quantity));

        Order order = new Order();
        order.setAccountId(accountId);
        order.setAmount(amount);
        order.setQuantity(quantity);
        order.setStockId(stockId);
        this.orderMapper.insertSelective(order);


        return order.getId();
    }

    @Override
    public Boolean updateOrder(Long accountId, Long orderId, Long stockId, Long quantity) {
        return this.orderMapper.updateOrder(accountId, orderId, stockId, quantity) > 0;
    }

    @Override
    public Integer createOrUpdateOrder(Long id, Long orderNumber, Long accountId, Long stockId, Long quantity, String note) {
        return this.orderMapper.createOrUpdateOrder(id, orderNumber, accountId, stockId, quantity, note);
    }

    @Override
    public Integer createOrUpdateOrder2(Long id, Long accountId, Long orderNumber, Long stockId, Long quantity, BigDecimal amount, String note) {
        return this.orderMapper.createOrUpdateOrder2(id, orderNumber, accountId, stockId, quantity, amount, note);
    }

    @Override
    public Boolean addOrUpdateStock(BigDecimal quantity, BigDecimal price) {
        return this.stockMapper.addOrUpdateStock(quantity, price);
    }

    @Override
    public Boolean addOrUpdateStock2(Long stockId, BigDecimal quantity, BigDecimal price) {
        return this.stockMapper.addOrUpdateStock2(stockId, quantity, price);
    }
}
