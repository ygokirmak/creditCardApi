package com.company.cc.customer.service;


import com.company.cc.customer.CustomerApplication;
import com.company.cc.customer.domain.Customer;
import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.repository.CustomerRepository;
import com.company.cc.customer.service.dto.AccountDTO;
import com.company.cc.customer.service.dto.CustomerDTO;
import com.company.cc.customer.service.dto.TransactionDTO;
import com.company.cc.customer.service.dto.TransactionSummaryDTO;
import com.company.cc.customer.service.impl.CustomerServiceImp;
import com.company.cc.customer.service.mapper.CustomerMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApplication.class)
@Transactional
public class CustomerServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionService transactionService;

    @Autowired
    private CustomerMapper customerMapper;

    CustomerService customerService;

    Customer customer;
    List<AccountDTO> accounts;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImp(customerRepository, customerMapper, accountService, transactionService);

        customer = new Customer();
        customer.setId(1l);
        customer.setName("test-name");
        customer.setSurname("test-surname");

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
    }


    @Test
    public void getOneCustomer() throws ServiceCommunicationException, EntityNotFoundException {
        Mockito.when(customerRepository.findById(anyLong()) )
                .thenReturn(Optional.of(customer));

        Mockito.when(accountService.getAccounts(anyLong()) )
                .thenReturn(accounts);

        Mockito.when(transactionService.getTransactionSummary(anyLong()) )
                .thenReturn(new TransactionSummaryDTO(1L,10.3d));

        CustomerDTO customerDTO = customerService.getOne(1L);
        assertThat(customerDTO.getName()).isEqualTo(customer.getName());
        assertThat(customerDTO.getSurname()).isEqualTo(customer.getSurname());
        assertThat(customerDTO.getAccounts()).isEqualTo(accounts);
        assertThat(customerDTO.getBalance()).isEqualTo(10.3d);

        verify(customerRepository, times(1)).findById(eq(1L));
        verify(accountService, times(1)).getAccounts(eq(1L));
        verify(transactionService, times(1)).getTransactionSummary(eq(1L));

        verifyNoMoreInteractions(customerRepository);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(transactionService);

    }



    @Test
    public void failedToGetOneCustomer_EntityNotFound() {

        Mockito.when(customerRepository.findById(anyLong()) )
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getOne(1L) )
                .isInstanceOf(EntityNotFoundException.class);

        verify(customerRepository, times(1)).findById(eq(1L));

        verifyNoMoreInteractions(customerRepository);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(transactionService);

    }

    @Test
    public void failedToGetOneCustomer_CommunicationErrorAccountService() throws ServiceCommunicationException {

        Mockito.when(customerRepository.findById(anyLong()) )
                .thenReturn(Optional.of(customer));

        Mockito.when(accountService.getAccounts(anyLong()) )
                .thenThrow(new ServiceCommunicationException("message"));

        assertThatThrownBy(() -> customerService.getOne(1L) )
                .isInstanceOf(ServiceCommunicationException.class);

        verify(customerRepository, times(1)).findById(eq(1L));
        verify(accountService, times(1)).getAccounts(eq(1L));

        verifyNoMoreInteractions(customerRepository);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(transactionService);

    }

    @Test
    public void failedToGetOneCustomer_CommunicationErrorTransactionService() throws ServiceCommunicationException {

        Mockito.when(customerRepository.findById(anyLong()) )
                .thenReturn(Optional.of(customer));

        Mockito.when(transactionService.getTransactionSummary(anyLong()) )
                .thenThrow(new ServiceCommunicationException("message2"));

        assertThatThrownBy(() -> customerService.getOne(1L) )
                .isInstanceOf(ServiceCommunicationException.class);

        verify(customerRepository, times(1)).findById(eq(1L));
        verify(accountService, times(1)).getAccounts(eq(1L));
        verify(transactionService, times(1)).getTransactionSummary(eq(1L));

        verifyNoMoreInteractions(customerRepository);
        verifyNoMoreInteractions(accountService);
        verifyNoMoreInteractions(transactionService);

    }




}
