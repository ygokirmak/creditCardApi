package com.company.cc.account.service;

import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.service.dto.AccountDTO;

public interface AccountService {

    AccountDTO findOne(long id) throws EntityNotFoundException;

    AccountDTO create(AccountDTO accountDTO) throws EntityAlreadyExistsException;

    AccountDTO update(AccountDTO accountDTO) throws EntityNotFoundException;

    AccountDTO delete(long id) throws EntityNotFoundException;

}
