package com.company.cc.account.exceptions;

import com.company.cc.account.service.dto.TransactionDTO;
import org.springframework.util.StringUtils;

public class TransactionCreationException extends Exception {

    public TransactionCreationException(TransactionDTO transactionDTO) {
        super(String.format("Cloudn't create transaction for object: %s ", transactionDTO.toString()));
    }
}