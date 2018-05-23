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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
@Transactional
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    private Account testAccount;

    @Before
    public void setup() {
        testAccount = new Account();
        testAccount.setId(1l);
        testAccount.setCustomerId(10l);
        testAccount.setName("test-name-1");
        testAccount.setSurname("test-surname-1");

        testAccount = accountRepository.save(testAccount);
    }

    @Test
    public void findById() throws EntityNotFoundException, TransactionFetchException {


        AccountDTO accountDTO = accountService.findOne(testAccount.getId());

        assertThat(accountDTO.getId()).isEqualTo(testAccount.getId());
        assertThat(accountDTO.getCustomerId()).isEqualTo(10l);
        assertThat(accountDTO.getName()).isEqualTo("test-name-1");
        assertThat(accountDTO.getSurname()).isEqualTo("test-surname-1");

    }

    @Test
    public void failedToFindById() throws EntityNotFoundException {

        assertThatThrownBy(() -> accountService.findOne(2) )
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Account was not found for parameters {id=2}");

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

        NewAccountDTO newAccountDTO = new NewAccountDTO();
        newAccountDTO.setCustomerId(20l);
        newAccountDTO.setInitialCredit(10l);

        AccountDTO createdAccount = accountService.create(newAccountDTO);
        assertThat(createdAccount.getId()).isNotNull();
        assertThat(createdAccount.getCustomerId()).isEqualTo(20l);

        // assertThat(createdAccount.getTransactions()).isEqualTo(20l);
    }


    @Test
    public void updateAccountBalance() throws EntityNotFoundException {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(testAccount.getId());
        accountDTO.setName("test-name-1");
        accountDTO.setSurname("test-surname-1");
        accountDTO.setCustomerId(1l);
        accountDTO.setBalance(20);

        AccountDTO updatedAccount = accountService.update(accountDTO);
        assertThat(updatedAccount.getId()).isEqualTo(testAccount.getId());
        assertThat(updatedAccount.getName()).isEqualTo("test-name-1");
        assertThat(updatedAccount.getSurname()).isEqualTo("test-surname-1");
        assertThat(updatedAccount.getBalance()).isEqualTo(20);


    }

}
