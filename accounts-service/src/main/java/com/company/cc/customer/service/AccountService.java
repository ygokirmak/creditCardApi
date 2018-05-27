package com.company.cc.customer.service;

import com.company.cc.customer.exceptions.EntityAlreadyExistsException;
import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.TransactionCreationException;
import com.company.cc.customer.exceptions.TransactionFetchException;
import com.company.cc.customer.service.dto.AccountDTO;
import com.company.cc.customer.service.dto.NewAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    AccountDTO getOne(long id) throws EntityNotFoundException, TransactionFetchException;

    AccountDTO create(NewAccountDTO newAccountDTO) throws EntityAlreadyExistsException, TransactionCreationException;

    AccountDTO delete(long id) throws EntityNotFoundException;

    Page<AccountDTO> getAccounts(Pageable pageable);
}
