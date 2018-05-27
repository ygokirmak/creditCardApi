package com.company.cc.customer.service.dto;

public class TransactionSummaryDTO {

    private Long customerId;

    private double balance;

    public TransactionSummaryDTO(Long customerId) {
        this.customerId = customerId;
    }

    public TransactionSummaryDTO(Long customerId, double balance) {
        this.customerId = customerId;
        this.balance = balance;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public TransactionSummaryDTO setCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public TransactionSummaryDTO setBalance(double balance) {
        this.balance = balance;
        return this;
    }
}
