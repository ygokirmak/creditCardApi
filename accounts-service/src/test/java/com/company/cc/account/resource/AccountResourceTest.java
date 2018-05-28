package com.company.cc.account.resource;


import com.company.cc.account.AccountApplication;
import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.RestExceptionHandler;
import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.service.AccountService;
import com.company.cc.shared.AccountDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
public class AccountResourceTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    AccountResource accountResource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        accountResource = new AccountResource(accountService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(accountResource)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

    }

    @Test
    public void failedGetCustomerAccount_NotExist() throws Exception {

        Mockito.when(accountService.getOne(ArgumentMatchers.anyLong()) )
                .thenThrow(new EntityNotFoundException(Object.class,"field","value"));

        mockMvc.perform(get("/api/accounts/" + Integer.MAX_VALUE))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).getOne(ArgumentMatchers.anyLong());
        verifyNoMoreInteractions(accountService);

    }

    @Test
    public void failedGetCustomerAccount_CommunicationException() throws Exception {

        Mockito.when(accountService.getOne(ArgumentMatchers.anyLong()) )
                .thenThrow(new ServiceCommunicationException("message"));

        mockMvc.perform(get("/api/accounts/" + Integer.MAX_VALUE))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).getOne(ArgumentMatchers.anyLong());
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void getAccount() throws Exception {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        accountDTO.setCustomerId(10L);
        accountDTO.setInitialCredit(10.3d);

        Mockito.when(accountService.getOne(ArgumentMatchers.anyLong()) )
                .thenReturn(accountDTO);

        mockMvc.perform(get("/api/accounts/" + 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerId", is(10)))
                .andExpect(jsonPath("$.initialCredit", is(10.3)));

        verify(accountService, times(1)).getOne(ArgumentMatchers.eq(1l));
        verifyNoMoreInteractions(accountService);

    }

}
