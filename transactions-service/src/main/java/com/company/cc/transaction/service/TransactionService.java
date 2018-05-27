package com.company.cc.transaction.service;

import com.company.cc.transaction.exceptions.EntityAlreadyExistsException;
import com.company.cc.transaction.exceptions.EntityNotFoundException;
import com.company.cc.transaction.service.dto.TransactionDTO;
import com.company.cc.transaction.service.dto.TransactionSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    TransactionDTO create(TransactionDTO TransactionDTO) throws EntityAlreadyExistsException;

    List<TransactionDTO> getByAccountId(Long accountId);

    TransactionSummaryDTO getSummaryByCustomerId(Long id) throws EntityNotFoundException;
}
