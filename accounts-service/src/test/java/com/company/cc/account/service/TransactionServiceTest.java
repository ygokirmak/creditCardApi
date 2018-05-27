package com.company.cc.account.service;


import com.company.cc.account.AccountApplication;
import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.service.dto.TransactionDTO;
import com.company.cc.account.service.impl.TransactionServiceImp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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
@SpringBootTest(classes = AccountApplication.class)
@Transactional
public class TransactionServiceTest {

    @Mock
    private RestTemplate restTemplate;

    TransactionService transactionService;

    List<TransactionDTO> transactions;
    ParameterizedTypeReference<List<TransactionDTO>> typeReference;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        transactionService = new TransactionServiceImp(restTemplate);

        TransactionDTO trx1_1 = new TransactionDTO();
        trx1_1.setAccountId(1l);
        trx1_1.setAmount(100l);
        trx1_1.setDirection("IN");

        TransactionDTO trx1_2 = new TransactionDTO();
        trx1_2.setAccountId(1l);
        trx1_2.setAmount(200l);
        trx1_2.setDirection("OUT");

        TransactionDTO trx2_1 = new TransactionDTO();
        trx2_1.setAccountId(2l);
        trx2_1.setAmount(200l);
        trx2_1.setDirection("IN");

        TransactionDTO trx2_2 = new TransactionDTO();
        trx2_2.setAccountId(2l);
        trx2_2.setAmount(100l);
        trx2_2.setDirection("OUT");

        transactions = new ArrayList<>();
        transactions.add(trx1_1);
        transactions.add(trx1_2);
        transactions.add(trx2_1);
        transactions.add(trx2_2);

        typeReference =
                new ParameterizedTypeReference<List<TransactionDTO>>() {};
    }

    @Test
    public void getTransactions() throws ServiceCommunicationException {

        Mockito.when(restTemplate.exchange(eq("http://transcations-service/api/transactions?accountId=1"),eq(HttpMethod.GET),eq(null),eq(typeReference)) )
                .thenReturn(new ResponseEntity<>(transactions, HttpStatus.OK));

        List<TransactionDTO> fetchedTransactions = transactionService.getTransactions(1L);

        assertThat(fetchedTransactions).isEqualTo(transactions);

        verify(restTemplate, times(1)).exchange(eq("http://transcations-service/api/transactions?accountId=1"),eq(HttpMethod.GET),eq(null), eq(typeReference));

        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void failedToGetTransactions_CommunicationException() throws ServiceCommunicationException {
        Mockito.when(restTemplate.exchange(anyString(),eq(HttpMethod.GET),eq(null),eq(typeReference)) )
                .thenThrow(new RestClientException("message"));

        assertThatThrownBy(() -> transactionService.getTransactions(1L))
                .isInstanceOf(ServiceCommunicationException.class);

    }



}
