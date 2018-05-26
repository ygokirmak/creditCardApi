package com.company.cc.transaction.service;


import com.company.cc.transaction.exceptions.EntityAlreadyExistsException;
import com.company.cc.transaction.exceptions.EntityNotFoundException;
import com.company.cc.transaction.TransactionApplication;
import com.company.cc.transaction.domain.Transaction;
import com.company.cc.transaction.domain.enumeration.TransactionDirection;
import com.company.cc.transaction.messages.Receiver;
import com.company.cc.transaction.repository.TransactionRepository;
import com.company.cc.transaction.service.dto.TransactionDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionApplication.class)
@Transactional
public class TransactionServiceTest {

    @Mock
    Receiver receiver;

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    private Transaction testTransaction;

    @Before
    public void setup() {
        testTransaction = new Transaction();
        testTransaction.setId(1l);
        testTransaction.setAccountId(1l);
        testTransaction.setDirection(TransactionDirection.IN);
        testTransaction.setAmount(100l);

        testTransaction = transactionRepository.save(testTransaction);
    }

    @Test
    public void findById() throws EntityNotFoundException {

        TransactionDTO transactionDTO = transactionService.findOne(testTransaction.getId());

        assertThat(transactionDTO.getId()).isEqualTo(transactionDTO.getId());
        assertThat(transactionDTO.getDirection()).isEqualTo(TransactionDirection.IN.toString());
        assertThat(transactionDTO.getAmount()).isEqualTo(100l);
        assertThat(transactionDTO.getAccountId()).isEqualTo(1l);

    }

    @Test
    public void failedToFindById() throws EntityNotFoundException {

        assertThatThrownBy(() -> transactionService.findOne(testTransaction.getId()+1) )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Transaction was not found for parameters {id=%d}",testTransaction.getId()+1));

    }

    @Test
    public void deleteById() throws EntityNotFoundException {

        TransactionDTO transactionDTO = transactionService.delete(testTransaction.getId());
        assertThat(transactionDTO.getId()).isEqualTo(testTransaction.getId());
    }

    @Test
    public void failedToDeleteById() throws EntityNotFoundException {

        assertThatThrownBy(() -> transactionService.delete(testTransaction.getId()+1) )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Transaction was not found for parameters {id=%d}",testTransaction.getId()+1));
    }

    @Test
    public void failedToCreateTransactionWithoutAccountId() throws EntityNotFoundException, EntityAlreadyExistsException {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(100l);
        transactionDTO.setDirection(TransactionDirection.OUT.toString());

        assertThatThrownBy(() -> transactionService.create(transactionDTO) )
                .isInstanceOf(ConstraintViolationException.class);

    }

}
