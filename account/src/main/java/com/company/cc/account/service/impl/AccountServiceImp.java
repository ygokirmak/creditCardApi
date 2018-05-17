package com.company.cc.account.service.impl;

import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.service.AccountService;
import com.company.cc.account.service.dto.AccountDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImp implements AccountService{

    @Override
    public AccountDTO findOne(long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public AccountDTO create(AccountDTO accountDTO) {
        return null;
    }

    @Override
    public AccountDTO update(AccountDTO accountDTO) throws EntityNotFoundException {
        return null;
    }

    @Override
    public AccountDTO delete(long id) throws EntityNotFoundException {
        return null;
    }
}
