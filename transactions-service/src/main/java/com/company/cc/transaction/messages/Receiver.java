package com.company.cc.transaction.messages;

import com.company.cc.transaction.exceptions.EntityAlreadyExistsException;
import com.company.cc.transaction.service.TransactionService;
import com.company.cc.shared.AccountDTO;
import com.company.cc.transaction.service.dto.TransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    TransactionService transactionService;

    public Receiver(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void receiveMessage(AccountDTO accountDTO) throws EntityAlreadyExistsException {

        if( accountDTO.getInitialCredit() > 0){
            transactionService.create(new TransactionDTO(accountDTO.getInitialCredit(), accountDTO.getCustomerId(),
                    accountDTO.getId(), "IN"));
        }
    }

}