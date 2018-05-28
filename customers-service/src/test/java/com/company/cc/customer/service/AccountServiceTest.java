package com.company.cc.customer.service;


import com.company.cc.customer.CustomerApplication;
import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.dto.AccountDTO;
import com.company.cc.customer.service.dto.TransactionDTO;
import com.company.cc.customer.service.impl.AccountServiceImp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApplication.class)
@Transactional
public class AccountServiceTest {

    @Mock
    private RestTemplate restTemplate;

    AccountService accountService;

    List<AccountDTO> accounts;
    ParameterizedTypeReference<List<AccountDTO>> typeReference;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountServiceImp(restTemplate);

        // account-1
        AccountDTO account1 = new AccountDTO();
        account1.setId(1l);
        account1.setCustomerId(1l);

        TransactionDTO trx1_1 = new TransactionDTO();
        trx1_1.setAccountId(1l);
        trx1_1.setAmount(100d);
        trx1_1.setDirection("IN");

        TransactionDTO trx1_2 = new TransactionDTO();
        trx1_2.setAccountId(1l);
        trx1_2.setAmount(200d);
        trx1_2.setDirection("OUT");

        List<TransactionDTO> account1Transactions = new ArrayList<>();
        account1Transactions.add(trx1_1);
        account1Transactions.add(trx1_2);

        account1.setTransactions(account1Transactions);

        // account-2
        AccountDTO account2 = new AccountDTO();
        account2.setId(2l);
        account2.setCustomerId(1l);

        TransactionDTO trx2_1 = new TransactionDTO();
        trx2_1.setAccountId(2l);
        trx2_1.setAmount(200d);
        trx2_1.setDirection("IN");

        TransactionDTO trx2_2 = new TransactionDTO();
        trx2_2.setAccountId(2l);
        trx2_2.setAmount(100d);
        trx2_2.setDirection("OUT");

        List<TransactionDTO> account2Transactions = new ArrayList<>();
        account2Transactions.add(trx2_1);
        account2Transactions.add(trx2_2);

        account2.setTransactions(account2Transactions);

        accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        typeReference =
                new ParameterizedTypeReference<List<AccountDTO>>() {};
    }

    @Test
    public void getCustomerAccounts() throws ServiceCommunicationException, EntityNotFoundException {

        Mockito.when(restTemplate.exchange(eq("http://accounts-service/api/accounts?customerId=1"),eq(HttpMethod.GET),eq(null),eq(typeReference)) )
                .thenReturn(new ResponseEntity<>(accounts, HttpStatus.OK));

        List<AccountDTO> fetchedAccounts = accountService.getAccounts(1l);

        assertThat(fetchedAccounts).isEqualTo(accounts);

        verify(restTemplate, times(1)).exchange(eq("http://accounts-service/api/accounts?customerId=1"),eq(HttpMethod.GET),eq(null),eq(typeReference));

        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void failedToGetCustomerAccounts_CommunicationException() throws ServiceCommunicationException {
        Mockito.when(restTemplate.exchange(eq("http://accounts-service/api/accounts?customerId=1"),eq(HttpMethod.GET),eq(null),eq(typeReference)) )
                .thenThrow(new RestClientException("message"));

        assertThatThrownBy(() -> accountService.getAccounts(1l))
                .isInstanceOf(ServiceCommunicationException.class);

    }



}
