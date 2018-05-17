package com.company.cc.transaction.service.dto;


import javax.validation.constraints.NotNull;

public class TransactionDTO {

    private Long id;

    private Long amount;
    private Long accountId;
    private String direction;

    public Long getId() {
        return id;
    }

    public TransactionDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getAmount() {
        return amount;
    }

    public TransactionDTO setAmount(Long amount) {
        this.amount = amount;
        return this;
    }

    public Long getAccountId() {
        return accountId;
    }

    public TransactionDTO setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public TransactionDTO setDirection(String direction) {
        this.direction = direction;
        return this;
    }
}
