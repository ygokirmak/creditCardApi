package com.company.cc.customer.exceptions;

public class TransactionFetchException extends Exception {

    public TransactionFetchException(Long accountId) {
        super(String.format("Couldn't fetch transactions for customer: %d ", accountId));
    }
}