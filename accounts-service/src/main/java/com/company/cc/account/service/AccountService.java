package com.company.cc.account.service;

import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.service.dto.AccountDTO;
import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.ServiceCommunicationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    AccountDTO getOne(long id) throws EntityNotFoundException, ServiceCommunicationException;

    AccountDTO create(AccountDTO newAccountDTO) throws EntityAlreadyExistsException, ServiceCommunicationException;

    Page<AccountDTO> getAccounts(Pageable pageable);
}
