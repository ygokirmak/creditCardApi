package com.company.cc.customer.service.dto;

import java.io.Serializable;
import java.util.List;

public class AccountDTO implements Serializable{

    private static final long serialVersionUID = 2850997990432943688L;

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
}
