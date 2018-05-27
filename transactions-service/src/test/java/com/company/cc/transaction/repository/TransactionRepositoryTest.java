package com.company.cc.transaction.repository;


import com.company.cc.transaction.TransactionApplication;
import com.company.cc.transaction.domain.Transaction;
import com.company.cc.transaction.domain.enumeration.TransactionDirection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionApplication.class)
@Transactional
public class TransactionRepositoryTest {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Before
    public void setup() throws ParseException {
        transactionRepository.deleteAll();
        
        transactionRepository.save(new Transaction(1L,1L,10d, TransactionDirection.IN));
        transactionRepository.save(new Transaction(1L,1L,20d, TransactionDirection.OUT));
        transactionRepository.save(new Transaction(1L,2L,5d, TransactionDirection.IN));
        transactionRepository.save(new Transaction(1L,2L,10d, TransactionDirection.IN));
        transactionRepository.save(new Transaction(1L,2L,15d, TransactionDirection.OUT));
        transactionRepository.save(new Transaction(2L,3L,10d, TransactionDirection.IN));
        transactionRepository.save(new Transaction(2L,3L,20d, TransactionDirection.OUT));
        transactionRepository.save(new Transaction(2L,3L,30d, TransactionDirection.IN));

    }

    @Test
    public void getBalanceByCustomerId() throws Exception {

        Optional<Double> balance = transactionRepository.getSummaryByCustomerId(1);
        assertThat(balance.get()).isEqualTo(-10);

    }

    @Test
    public void getBalanceByCustomerId_NoTransaction() throws Exception {

        Optional<Double> balance = transactionRepository.getSummaryByCustomerId(3);
        assertThat(balance.isPresent()).isFalse();

    }

}
