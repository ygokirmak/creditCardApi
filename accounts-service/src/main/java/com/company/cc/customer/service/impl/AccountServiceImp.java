package com.company.cc.customer.service.impl;

import com.company.cc.customer.domain.Account;
import com.company.cc.customer.exceptions.EntityAlreadyExistsException;
import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.TransactionCreationException;
import com.company.cc.customer.exceptions.TransactionFetchException;
import com.company.cc.customer.repository.AccountRepository;
import com.company.cc.customer.service.AccountService;
import com.company.cc.customer.service.dto.AccountDTO;
import com.company.cc.customer.service.dto.NewAccountDTO;
import com.company.cc.customer.service.dto.TransactionDTO;
import com.company.cc.customer.service.mapper.AccountMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImp implements AccountService{

    AccountRepository accountRepository;
    AccountMapper accountMapper;
    RabbitTemplate rabbitTemplate;

    RestTemplate restTemplate;


    public AccountServiceImp(AccountRepository accountRepository, AccountMapper accountMapper,
                             RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
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
                    restTemplate.exchange("http://transactions-service/api/transactions?accountId="+id, HttpMethod.GET, null, typeReference);

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

        rabbitTemplate.convertAndSend("customer-created", "foo.bar.baz", "Hello from RabbitMQ!");


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
            TransactionDTO trx = restTemplate.postForObject("http://transactions-service/api/transactions", request, TransactionDTO.class);
        } catch (RestClientException ex){
            throw new TransactionCreationException(transactionDTO);
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
    public Page<AccountDTO> getAccounts(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        return accounts.map( accountMapper::toDto);
    }
}