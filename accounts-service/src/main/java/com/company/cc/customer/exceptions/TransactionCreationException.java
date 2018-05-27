package com.company.cc.customer.exceptions;

import com.company.cc.customer.service.dto.TransactionDTO;

public class TransactionCreationException extends Exception {

    public TransactionCreationException(TransactionDTO transactionDTO) {
        super(String.format("Couldn't create transaction for object: %s ", transactionDTO.toString()));
    }
}