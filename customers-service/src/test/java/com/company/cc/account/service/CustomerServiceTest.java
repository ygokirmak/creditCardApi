package com.company.cc.account.service;


import com.company.cc.account.CustomerApplication;
import com.company.cc.account.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApplication.class)
@Transactional
public class CustomerServiceTest {

    @Mock
    private RestTemplate restTemplate;

    CustomerService accountService;

    @Autowired
    CustomerRepository accountRepository;

    @Test
    public void dummy() {

        assertThat(true).isTrue();

    }

}
