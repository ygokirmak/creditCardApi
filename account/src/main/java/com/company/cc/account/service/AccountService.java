package com.company.cc.account.service;

import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.TransactionCreationException;
import com.company.cc.account.exceptions.TransactionFetchException;
import com.company.cc.account.service.dto.AccountDTO;
import com.company.cc.account.service.dto.NewAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {

    AccountDTO getOne(long id) throws EntityNotFoundException, TransactionFetchException;

    AccountDTO create(NewAccountDTO newAccountDTO) throws EntityAlreadyExistsException, TransactionCreationException;

    AccountDTO update(AccountDTO accountDTO) throws EntityNotFoundException;

    AccountDTO delete(long id) throws EntityNotFoundException;

    List<AccountDTO> getAccounts();
}
