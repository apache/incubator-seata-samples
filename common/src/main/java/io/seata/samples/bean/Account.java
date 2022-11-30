package io.seata.samples.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "sys_account")
public class Account implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public Account setId(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Account setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    @Override
    public String toString() {
        return "Account{" +
            "id=" + id +
            ", balance=" + balance +
            '}';
    }
}
