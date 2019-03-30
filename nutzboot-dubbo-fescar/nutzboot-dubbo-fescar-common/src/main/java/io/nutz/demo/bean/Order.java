package io.nutz.demo.bean;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("order_tbl")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private long id;
    @Column("user_id")
    private String userId;
    @Column("commodity_code")
    private String commodityCode;
    @Column
    private int count;
    @Column
    private int money;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getCommodityCode() {
        return commodityCode;
    }
    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
}
