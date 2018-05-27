package com.company.cc.account.service.dto;

import java.util.List;
import java.util.Objects;

public class AccountDTO {

    private Long id;

    private Long customerId;

    private double initialCredit;

    private List<TransactionDTO> transactions;


    public Long getId() {
        return id;
    }

    public AccountDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public AccountDTO setCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public AccountDTO setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
        return this;
    }

    public double getInitialCredit() {
        return initialCredit;
    }

    public AccountDTO setInitialCredit(double initialCredit) {
        this.initialCredit = initialCredit;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDTO)) return false;
        AccountDTO that = (AccountDTO) o;
        return initialCredit == that.initialCredit &&
                Objects.equals(id, that.id) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, initialCredit, transactions);
    }
}
