package io.seata.samples.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "sys_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    private Long accountId;
    private Long stockId;
    private Long quantity;
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public Order setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Order setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public Long getStockId() {
        return stockId;
    }

    public Order setStockId(Long stockId) {
        this.stockId = stockId;
        return this;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Order setQuantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Order setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", accountId=" + accountId +
            ", stockId=" + stockId +
            ", quantity=" + quantity +
            ", amount=" + amount +
            '}';
    }
}
