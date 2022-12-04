/*
 *  Copyright 1999-2022 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
    private Long orderNumber;
    private String note;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
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
