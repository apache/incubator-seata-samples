package io.seata.samples.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "sys_stock")
public class Stock implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    private Long quantity;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public Stock setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Stock setQuantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Stock setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + id +
            ", quantity=" + quantity +
            ", price=" + price +
            '}';
    }
}
