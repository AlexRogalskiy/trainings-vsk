package com.reunico.training.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable {
    @JsonAlias("name.findName")
    private String fullName;

    @JsonAlias("name.title")
    private String title;

    @JsonAlias("random.number")
    private Long number;

    @JsonAlias("finance.amount")
    private Double amount;

    @JsonAlias("address.countryCode")
    private String countryCode;

    /*

        Для сериализации с помощью Jackson необходимо иметь пустой конструктор и реализацию Serializable

     */
    public Customer() {
    }

    public Customer(Double amount, String countryCode) {
        this.amount = amount;
        this.countryCode = countryCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
