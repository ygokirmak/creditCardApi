package com.company.cc.account.service.impl;

import com.company.cc.account.domain.Account;
import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.repository.AccountRepository;
import com.company.cc.account.service.AccountService;
import com.company.cc.account.service.NotificationService;
import com.company.cc.account.service.TransactionService;
import com.company.cc.account.service.dto.AccountDTO;
import com.company.cc.account.service.dto.TransactionDTO;
import com.company.cc.account.service.mapper.AccountMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImp implements AccountService {

    AccountRepository accountRepository;
    AccountMapper accountMapper;
    NotificationService notificationService;
    TransactionService transactionService;


    public AccountServiceImp(AccountRepository accountRepository, AccountMapper accountMapper,
                             NotificationService notificationService,  TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.notificationService = notificationService;
        this.transactionService = transactionService;
    }

    @Override
    public AccountDTO getOne(long id) throws EntityNotFoundException, ServiceCommunicationException {

        Optional<Account> account = accountRepository.findById(id);

        if( account.isPresent() ){
            AccountDTO accountDTO = accountMapper.toDto(account.get());
            List<TransactionDTO> transactions = transactionService.getTransactions(accountDTO.getId());
            accountDTO.setTransactions(transactions);
            return accountDTO;
        }else{
            throw new EntityNotFoundException(Account.class, "id", String.valueOf(id));
        }
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public AccountDTO create(AccountDTO accountDTO) throws EntityAlreadyExistsException, ServiceCommunicationException {

        Account account = accountMapper.toEntity(accountDTO);
        account = accountRepository.save(account);

        notificationService.accountCreatedNotification(accountMapper.toDto(account));

        return accountMapper.toDto(account);
    }


    @Override
    public Page<AccountDTO> getAccounts(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        return accounts.map( accountMapper::toDto);
    }
}
