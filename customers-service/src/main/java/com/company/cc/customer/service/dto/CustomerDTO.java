package com.company.cc.customer.service.dto;

import java.util.List;

public class CustomerDTO {

    private Long id;

    private String name;

    private String surname;

    private double balance;

    private List<AccountDTO> accounts;


    public Long getId() {
        return id;
    }

    public CustomerDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CustomerDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public CustomerDTO setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public CustomerDTO setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public List<AccountDTO> getAccounts() {
        return accounts;
    }

    public CustomerDTO setAccounts(List<AccountDTO> accounts) {
        this.accounts = accounts;
        return this;
    }
}
