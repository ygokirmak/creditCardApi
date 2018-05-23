package com.company.cc.transaction.service;

import com.company.cc.transaction.exceptions.EntityAlreadyExistsException;
import com.company.cc.transaction.exceptions.EntityNotFoundException;
import com.company.cc.transaction.service.dto.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    TransactionDTO findOne(long id) throws EntityNotFoundException;

    TransactionDTO create(TransactionDTO TransactionDTO) throws EntityAlreadyExistsException;

    TransactionDTO update(TransactionDTO TransactionDTO) throws EntityNotFoundException;

    TransactionDTO delete(long id) throws EntityNotFoundException;

    List<TransactionDTO> getByAccountId(Long accountId);
}
