package com.company.cc.account.service.impl;

import com.company.cc.account.domain.Account;
import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.repository.AccountRepository;
import com.company.cc.account.service.AccountService;
import com.company.cc.account.service.dto.AccountDTO;
import com.company.cc.account.service.mapper.AccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountServiceImp implements AccountService{

    AccountRepository accountRepository;
    AccountMapper accountMapper;

    public AccountServiceImp(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountDTO findOne(long id) throws EntityNotFoundException {

        Optional<Account> account = accountRepository.findById(id);

        if( account.isPresent() ){
            return accountMapper.toDto(account.get());
        }else{
            throw new EntityNotFoundException(Account.class, "id", String.valueOf(id));
        }
    }

    @Override
    public AccountDTO create(AccountDTO accountDTO) throws EntityAlreadyExistsException {

        if( accountDTO.getId() != null){
            throw new EntityAlreadyExistsException(Account.class, accountDTO.getId());
        }

        Account account = accountMapper.toEntity(accountDTO);
        account = accountRepository.save(account);

        return accountMapper.toDto(account);
    }

    @Override
    public AccountDTO update(AccountDTO accountDTO) throws EntityNotFoundException {

        Optional<Account> account = accountRepository.findById(accountDTO.getId());

        if( account.isPresent() ){
            Account saved = accountRepository.save(accountMapper.toEntity(accountDTO));
            return accountMapper.toDto(saved);

        }else{
            throw new EntityNotFoundException(Account.class, "id", String.valueOf(accountDTO.getId()));
        }
    }

    @Override
    public AccountDTO delete(long id) throws EntityNotFoundException {

        Optional<Account> account = accountRepository.findById(id);

        if( account.isPresent() ){
            accountRepository.deleteById(id);

            return new AccountDTO().setId(id);

        }else{
            throw new EntityNotFoundException(Account.class, "id", String.valueOf(id));
        }

    }
}
