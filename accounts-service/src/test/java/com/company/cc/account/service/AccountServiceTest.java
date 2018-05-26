package com.company.cc.account.service;


import com.company.cc.account.AccountApplication;
import com.company.cc.account.domain.Account;
import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.TransactionCreationException;
import com.company.cc.account.exceptions.TransactionFetchException;
import com.company.cc.account.repository.AccountRepository;
import com.company.cc.account.service.dto.AccountDTO;
import com.company.cc.account.service.dto.NewAccountDTO;
import com.company.cc.account.service.dto.TransactionDTO;
import com.company.cc.account.service.impl.AccountServiceImp;
import com.company.cc.account.service.mapper.AccountMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.matcher.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
@Transactional
public class AccountServiceTest {

    @Mock
    private RestTemplate restTemplate;

    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;

    private Account testAccount;

    @Before
    public void setup() {
        testAccount = new Account();
        testAccount.setId(1l);
        testAccount.setCustomerId(10l);
        testAccount.setName("test-name-1");
        testAccount.setSurname("test-surname-1");

        testAccount = accountRepository.save(testAccount);

        MockitoAnnotations.initMocks(this);

        accountService = new AccountServiceImp(accountRepository,accountMapper,restTemplate);

    }

    @Test
    public void getById() throws EntityNotFoundException, TransactionFetchException {

        List<TransactionDTO> transactions = new ArrayList<>();
        transactions.add(new TransactionDTO(10l, testAccount.getId(), "IN"));
        transactions.add(new TransactionDTO(20l, testAccount.getId(), "IN"));

        ResponseEntity<List<TransactionDTO>> response = new ResponseEntity<>(transactions, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(anyString(), ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.<ParameterizedTypeReference<List<TransactionDTO>>>any() )
        ).thenReturn(response);

        AccountDTO accountDTO = accountService.getOne(testAccount.getId());

        assertThat(accountDTO.getId()).isEqualTo(testAccount.getId());
        assertThat(accountDTO.getCustomerId()).isEqualTo(10l);
        assertThat(accountDTO.getName()).isEqualTo("test-name-1");
        assertThat(accountDTO.getSurname()).isEqualTo("test-surname-1");
        assertThat(accountDTO.getBalance()).isEqualTo(30l);

    }

    @Test
    public void getByIdWithMultipleTransactions() throws EntityNotFoundException, TransactionFetchException {

        List<TransactionDTO> transactions = new ArrayList<>();
        transactions.add(new TransactionDTO(10l, testAccount.getId(), "OUT"));
        transactions.add(new TransactionDTO(20l, testAccount.getId(), "IN"));
        transactions.add(new TransactionDTO(40l, testAccount.getId(), "OUT"));
        transactions.add(new TransactionDTO(100l, testAccount.getId(), "IN"));
        transactions.add(new TransactionDTO(220l, testAccount.getId(), "OUT"));

        ResponseEntity<List<TransactionDTO>> response = new ResponseEntity<>(transactions, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(anyString(), ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.<ParameterizedTypeReference<List<TransactionDTO>>>any() )
        ).thenReturn(response);

        AccountDTO accountDTO = accountService.getOne(testAccount.getId());

        assertThat(accountDTO.getId()).isEqualTo(testAccount.getId());
        assertThat(accountDTO.getCustomerId()).isEqualTo(10l);
        assertThat(accountDTO.getName()).isEqualTo("test-name-1");
        assertThat(accountDTO.getSurname()).isEqualTo("test-surname-1");
        assertThat(accountDTO.getBalance()).isEqualTo(-150l);

    }

    @Test
    public void failedToGetAccountById_NonExists() throws EntityNotFoundException {

        assertThatThrownBy(() -> accountService.getOne(testAccount.getId()+1) )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Account was not found for parameters {id=%d}",testAccount.getId()+1));

    }

    @Test
    public void failedToGetAccountById_TrxError() throws EntityNotFoundException {

        Mockito.when(restTemplate.exchange(anyString(), ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.<ParameterizedTypeReference<List<TransactionDTO>>>any() )
        ).thenThrow(new RestClientException("transaction service exception"));

        assertThatThrownBy(() -> accountService.getOne(testAccount.getId()) )
                .isInstanceOf(TransactionFetchException.class)
                .hasMessageContaining(String.format("Couldn't fetch transactions for account: %d ",testAccount.getId()));

    }


    @Test
    public void deleteById() throws EntityNotFoundException {

        AccountDTO accountDTO = accountService.delete(testAccount.getId());
        assertThat(accountDTO.getId()).isEqualTo(testAccount.getId());
    }

    @Test
    public void failedToDeleteById() throws EntityNotFoundException {

        assertThatThrownBy(() -> accountService.delete(testAccount.getId()+1) )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Account was not found for parameters {id=%d}",testAccount.getId()+1));
    }

    @Test
    public void createAccountWithCustomerId() throws EntityNotFoundException, EntityAlreadyExistsException, TransactionCreationException {

        NewAccountDTO newAccountDTO = new NewAccountDTO();
        newAccountDTO.setCustomerId(20l);
        newAccountDTO.setInitialCredit(0);

        AccountDTO createdAccount = accountService.create(newAccountDTO);
        assertThat(createdAccount.getId()).isNotNull();
        assertThat(createdAccount.getCustomerId()).isEqualTo(20l);
        assertThat(createdAccount.getBalance()).isEqualTo(0);

    }

    @Test
    public void createAccountWithCustomerIdAndBalance() throws EntityAlreadyExistsException, TransactionCreationException {

        Mockito.when(restTemplate.postForObject(ArgumentMatchers.any(), ArgumentMatchers.any(),ArgumentMatchers.any() )
        ).thenReturn(any());

        NewAccountDTO newAccountDTO = new NewAccountDTO();
        newAccountDTO.setCustomerId(20l);
        newAccountDTO.setInitialCredit(10l);

        AccountDTO createdAccount = accountService.create(newAccountDTO);
        assertThat(createdAccount.getId()).isNotNull();
        assertThat(createdAccount.getCustomerId()).isEqualTo(20l);

    }

    @Test
    public void failedToCreateAccount_TrxError() throws EntityAlreadyExistsException, TransactionCreationException {

        Mockito.when(restTemplate.postForObject(anyString(), ArgumentMatchers.any(),ArgumentMatchers.any() )
        ).thenThrow(new RestClientException("transaction service exception"));

        NewAccountDTO newAccountDTO = new NewAccountDTO();
        newAccountDTO.setCustomerId(20l);
        newAccountDTO.setInitialCredit(10l);

        assertThatThrownBy(() -> accountService.create(newAccountDTO) )
                .isInstanceOf(TransactionCreationException.class);

    }


}
