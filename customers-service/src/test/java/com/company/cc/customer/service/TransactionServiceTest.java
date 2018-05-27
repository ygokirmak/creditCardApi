package com.company.cc.customer.service;


import com.company.cc.customer.CustomerApplication;
import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.dto.TransactionSummaryDTO;
import com.company.cc.customer.service.impl.TransactionServiceImp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApplication.class)
@Transactional
public class TransactionServiceTest {

    @Mock
    private RestTemplate restTemplate;

    TransactionService transactionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        transactionService = new TransactionServiceImp(restTemplate);

    }

    @Test
    public void getTransactionsSummary() throws ServiceCommunicationException, EntityNotFoundException {

        Mockito.when(restTemplate.exchange(eq("http://transactions-service/api/transaction-summary/1"),eq(HttpMethod.GET),eq(null),eq(TransactionSummaryDTO.class)) )
                .thenReturn(new ResponseEntity<>(new TransactionSummaryDTO(1l,10.1d), HttpStatus.OK));

        TransactionSummaryDTO summaryDTO = transactionService.getTransactionSummary(1l);

        assertThat(summaryDTO.getBalance()).isEqualTo(10.1d);

        verify(restTemplate, times(1)).exchange(eq("http://transactions-service/api/transaction-summary/1"),eq(HttpMethod.GET),eq(null),eq(TransactionSummaryDTO.class));

        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void failedToGetTransactionsSummary_CommunicationException() throws ServiceCommunicationException {
        Mockito.when(restTemplate.exchange(eq("http://transactions-service/api/transaction-summary/1"),eq(HttpMethod.GET),eq(null),eq(TransactionSummaryDTO.class)) )
                .thenThrow(new RestClientException("message"));

        assertThatThrownBy(() -> transactionService.getTransactionSummary(1L))
                .isInstanceOf(ServiceCommunicationException.class);

    }

    @Test
    public void failedToGetTransactionsSummary_NoTransaction() throws ServiceCommunicationException {
        Mockito.when(restTemplate.exchange(eq("http://transactions-service/api/transaction-summary/1"),eq(HttpMethod.GET),eq(null),eq(TransactionSummaryDTO.class)) )
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        TransactionSummaryDTO summaryDTO = transactionService.getTransactionSummary(1L);

        assertThat(summaryDTO.getCustomerId()).isEqualTo(1L);
        assertThat(summaryDTO.getBalance()).isEqualTo(0);


    }


}
