package com.reunico.training.model;

import java.io.Serializable;

public class Order implements Serializable {
    private String customer;
    private String dish;

    public Order() {
    }

    public Order(String customer, String dish) {
        this.customer = customer;
        this.dish = dish;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }
}
