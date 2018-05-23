package com.company.cc.account.exceptions;

import com.company.cc.account.service.dto.TransactionDTO;

public class TransactionFetchException extends Exception {

    public TransactionFetchException(Long accountId) {
        super(String.format("Cloudn't fetch transactions for account: %d ", accountId));
    }
}