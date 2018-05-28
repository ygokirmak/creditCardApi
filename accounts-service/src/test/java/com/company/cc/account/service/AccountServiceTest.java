package com.company.cc.account.service;


import com.company.cc.account.AccountApplication;
import com.company.cc.account.domain.Account;
import com.company.cc.account.exceptions.EntityAlreadyExistsException;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.repository.AccountRepository;
import com.company.cc.shared.AccountDTO;
import com.company.cc.shared.TransactionDTO;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
public class AccountServiceTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private TransactionService transactionService;

    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountMapper accountMapper;

    private Account testAccount;

    @Before
    public void setup() {

        accountRepository.deleteAll();

        testAccount = new Account();
        testAccount.setId(1l);
        testAccount.setCustomerId(10l);

        testAccount = accountRepository.save(testAccount);

        MockitoAnnotations.initMocks(this);

        accountService = new AccountServiceImp(accountRepository,accountMapper,notificationService,transactionService);

    }

    @Test
    public void getById() throws EntityNotFoundException, ServiceCommunicationException {

        List<TransactionDTO> transactions = new ArrayList<>();
        transactions.add(new TransactionDTO(10d,testAccount.getCustomerId(), testAccount.getId(), "IN"));
        transactions.add(new TransactionDTO(20d, testAccount.getCustomerId(), testAccount.getId(), "IN"));

        Mockito.when(transactionService.getTransactions(anyLong()))
                .thenReturn(transactions);

        AccountDTO accountDTO = accountService.getOne(testAccount.getId());

        assertThat(accountDTO.getId()).isEqualTo(testAccount.getId());
        assertThat(accountDTO.getCustomerId()).isEqualTo(10l);
        assertThat(accountDTO.getTransactions()).isEqualTo(transactions);

        verify(transactionService, times(1)).getTransactions(anyLong());

    }

    @Test
    public void failedToGetAccountById_NonExists() throws EntityNotFoundException {

        assertThatThrownBy(() -> accountService.getOne(testAccount.getId()+1) )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Account was not found for parameters {id=%d}",testAccount.getId()+1));

    }

    @Test
    public void failedToGetAccountById_CommunicationException() throws ServiceCommunicationException {

        Mockito.when(transactionService.getTransactions(anyLong()))
                .thenThrow(new ServiceCommunicationException("message"));

        assertThatThrownBy(() -> accountService.getOne(testAccount.getId()) )
                .isInstanceOf(ServiceCommunicationException.class);

    }


    @Test
    public void createAccountWithCustomerId() throws EntityNotFoundException, EntityAlreadyExistsException, ServiceCommunicationException {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCustomerId(20l);
        accountDTO.setInitialCredit(0);

        AccountDTO createdAccount = accountService.create(accountDTO);
        assertThat(createdAccount.getId()).isNotNull();
        assertThat(createdAccount.getCustomerId()).isEqualTo(20l);
        assertThat(createdAccount.getInitialCredit()).isEqualTo(0);

    }

    @Test
    public void createAccountWithInitialCredit() throws EntityAlreadyExistsException, ServiceCommunicationException {

        Mockito.doNothing().when(notificationService).accountCreatedNotification(any(AccountDTO.class));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCustomerId(20l);
        accountDTO.setInitialCredit(10l);

        AccountDTO createdAccount = accountService.create(accountDTO);

        verify(notificationService, times(1)).accountCreatedNotification(any(AccountDTO.class));

        assertThat(createdAccount.getId()).isNotNull();
        assertThat(createdAccount.getCustomerId()).isEqualTo(20l);
        assertThat(createdAccount.getInitialCredit()).isEqualTo(10l);

    }

    @Test
    public void failedToCreateAccount_CommunicationException() throws EntityAlreadyExistsException, ServiceCommunicationException {

        Mockito.doThrow(new ServiceCommunicationException("message"))
                .when(notificationService).accountCreatedNotification(any(AccountDTO.class));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCustomerId(20l);
        accountDTO.setInitialCredit(10l);

        assertThatThrownBy(() -> accountService.create(accountDTO) )
                .isInstanceOf(ServiceCommunicationException.class);

    }


}
