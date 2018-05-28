package com.company.cc.account.service;

import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.shared.AccountDTO;

import java.util.List;

public interface AccountService {

    AccountDTO getOne(long id) throws EntityNotFoundException, ServiceCommunicationException;

    AccountDTO create(AccountDTO newAccountDTO) throws EntityAlreadyExistsException, ServiceCommunicationException;

    List<AccountDTO> getAccounts(Long customerId) throws ServiceCommunicationException;
}
