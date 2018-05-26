package com.company.cc.account.service.impl;

import com.company.cc.account.domain.Account;
import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.TransactionCreationException;
import com.company.cc.account.exceptions.TransactionFetchException;
import com.company.cc.account.repository.AccountRepository;
import com.company.cc.account.service.AccountService;
import com.company.cc.account.service.dto.AccountDTO;
import com.company.cc.account.service.dto.NewAccountDTO;
import com.company.cc.account.service.dto.TransactionDTO;
import com.company.cc.account.service.mapper.AccountMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService{

    AccountRepository accountRepository;
    AccountMapper accountMapper;

    RestTemplate restTemplate;


    public AccountServiceImp(AccountRepository accountRepository, AccountMapper accountMapper,
                             RestTemplate restTemplate) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public AccountDTO getOne(long id) throws EntityNotFoundException, TransactionFetchException {

        Optional<Account> account = accountRepository.findById(id);

        if( account.isPresent() ){
            AccountDTO accountDTO = accountMapper.toDto(account.get());
            List<TransactionDTO> transactions = getAccountTransactions(accountDTO.getId());
            long balance = calculateBalance(transactions);
            accountDTO.setTransactions(transactions);
            accountDTO.setBalance(balance);
            return accountDTO;
        }else{
            throw new EntityNotFoundException(Account.class, "id", String.valueOf(id));
        }
    }

    private long calculateBalance(List<TransactionDTO> transactions) {
        long balance = 0;

        for (TransactionDTO trx: transactions) {
            balance += trx.getDirection().equals("IN") ? trx.getAmount(): ( trx.getAmount()* -1);
        }

        return  balance;
    }

    private List<TransactionDTO> getAccountTransactions(Long id) throws TransactionFetchException {
        try {
            ParameterizedTypeReference<List<TransactionDTO>> typeReference =
                    new ParameterizedTypeReference<List<TransactionDTO>>() {};

            ResponseEntity<List<TransactionDTO>> response =
                    restTemplate.exchange("http://transaction/api/transactions?accountId="+id, HttpMethod.GET, null, typeReference);

            return response.getBody();
        } catch (RestClientException ex){
            throw new TransactionFetchException(id);
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public AccountDTO create(NewAccountDTO newAccountDTO) throws EntityAlreadyExistsException, TransactionCreationException {

        Account account = new Account();
        account.setCustomerId(newAccountDTO.getCustomerId());
        account = accountRepository.save(account);

        if( newAccountDTO.getInitialCredit() > 0){
            createTransaction(account, newAccountDTO.getInitialCredit());
        }

        return accountMapper.toDto(account);
    }

    private void createTransaction(Account account, long initialCredit) throws TransactionCreationException {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(account.getId());
        transactionDTO.setDirection("IN");
        transactionDTO.setAmount(initialCredit);

        createTransaction(transactionDTO);

    }

    private void createTransaction(TransactionDTO transactionDTO) throws TransactionCreationException {

        try {
            HttpEntity<TransactionDTO> request = new HttpEntity<>(transactionDTO);
            TransactionDTO trx = restTemplate.postForObject("http://transaction/api/transactions", request, TransactionDTO.class);
        } catch (RestClientException ex){
            throw new TransactionCreationException(transactionDTO);
        }

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

    @Override
    public List<AccountDTO> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map( accountMapper::toDto).collect(Collectors.toList());
    }
}
