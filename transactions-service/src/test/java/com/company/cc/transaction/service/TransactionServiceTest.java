package com.company.cc.transaction.service;

import com.company.cc.transaction.TransactionApplication;
import com.company.cc.transaction.domain.Transaction;
import com.company.cc.transaction.domain.enumeration.TransactionDirection;
import com.company.cc.transaction.repository.TransactionRepository;
import com.company.cc.transaction.service.dto.TransactionDTO;
import com.company.cc.transaction.service.impl.TransactionServiceImp;
import com.company.cc.transaction.service.mapper.TransactionMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionApplication.class)
@Transactional
public class TransactionServiceTest {

    @Autowired
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    private List<Transaction> transactions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        transactionService = new TransactionServiceImp(transactionRepository, transactionMapper);

        transactions = new ArrayList<>();

        transactions.add(new Transaction(1L,1L,10d, TransactionDirection.IN));
        transactions.add(new Transaction(1L,1L,20d, TransactionDirection.OUT));
        transactions.add(new Transaction(1L,2L,5d, TransactionDirection.IN));
        transactions.add(new Transaction(1L,2L,10d, TransactionDirection.IN));
        transactions.add(new Transaction(1L,2L,15d, TransactionDirection.OUT));
        transactions.add(new Transaction(2L,3L,10d, TransactionDirection.IN));
        transactions.add(new Transaction(2L,3L,20d, TransactionDirection.OUT));
        transactions.add(new Transaction(2L,3L,30d, TransactionDirection.IN));

    }

    @Test
    public void getTransactionsByAccountId() {

        Mockito.when(transactionRepository.findByAccountId(anyLong()) )
                .thenReturn(transactions);

        List<TransactionDTO> transactionDTOS = transactionService.getByAccountId(1l);

        assertThat(transactionDTOS.size()).isEqualTo(transactions.size());

        verify(transactionRepository, times(1)).findByAccountId(eq(1l));

        verifyNoMoreInteractions(transactionRepository);
    }




}
