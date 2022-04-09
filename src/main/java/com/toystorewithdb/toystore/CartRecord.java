package com.toystorewithdb.toystore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cart")
public class CartRecord {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer cartid;
    private String username;
    private int toyid;
    private String name;
    private String description;

    CartRecord() {}

    public void setCartid(Integer id) {
        this.cartid = id;
    }
    public Integer getCartid() {
        return cartid;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setToyid(Integer toyid) {
        this.toyid = toyid;
    }
    public Integer getToyid() {
        return toyid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setDescription(String desc) {
        this.description = desc;
    }
    public String getDescription() {
        return description;
    }
}
