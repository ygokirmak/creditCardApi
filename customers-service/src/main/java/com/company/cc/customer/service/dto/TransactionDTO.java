package com.company.cc.customer.service.dto;

import java.util.Objects;

public class TransactionDTO {

    private Long id;

    private Double amount;
    private Long customerId;
    private Long accountId;
    private String direction;

    public Long getId() {
        return id;
    }

    public TransactionDTO() {
    }

    public TransactionDTO(Double amount, Long customerId, Long accountId, String direction) {
        this.amount = amount;
        this.customerId = customerId;
        this.accountId = accountId;
        this.direction = direction;
    }

    public TransactionDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public TransactionDTO setAmount(Double amount) {
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

    public Long getCustomerId() {
        return customerId;
    }

    public TransactionDTO setCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionDTO)) return false;
        TransactionDTO that = (TransactionDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, customerId, accountId, direction);
    }
}
