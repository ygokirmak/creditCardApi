package com.company.cc.transaction.service;

import com.company.cc.shared.TransactionDTO;
import com.company.cc.transaction.exceptions.EntityAlreadyExistsException;
import com.company.cc.transaction.exceptions.EntityNotFoundException;
import com.company.cc.transaction.service.dto.TransactionSummaryDTO;

import java.util.List;

public interface TransactionService {

    TransactionDTO create(TransactionDTO TransactionDTO) throws EntityAlreadyExistsException;

    List<TransactionDTO> getByAccountId(Long accountId);

    TransactionSummaryDTO getSummaryByCustomerId(Long id);
}
