package com.company.cc.customer.resource;


import com.company.cc.customer.CustomerApplication;
import com.company.cc.customer.domain.Customer;
import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.RestExceptionHandler;
import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.CustomerService;
import com.company.cc.customer.service.dto.CustomerDTO;
import com.company.cc.customer.service.enumeration.CustomerView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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
@SpringBootTest(classes = CustomerApplication.class)
public class CustomerResourceTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    CustomerResource customerResource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        customerResource = new CustomerResource(customerService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(customerResource)
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

    }

    @Test
    public void getNonExistingCustomer() throws Exception {

        Mockito.when(customerService.getOne(ArgumentMatchers.any(CustomerView.class), ArgumentMatchers.anyLong()) )
                .thenThrow(new EntityNotFoundException(Object.class,"field","value"));

        mockMvc.perform(get("/api/customers/" + Integer.MAX_VALUE))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).getOne(ArgumentMatchers.any(CustomerView.class), ArgumentMatchers.anyLong());
        verifyNoMoreInteractions(customerService);

    }

    @Test
    public void failedGetCustomerAccount() throws Exception {

        Mockito.when(customerService.getOne(ArgumentMatchers.any(CustomerView.class), ArgumentMatchers.anyLong()) )
                .thenThrow(new ServiceCommunicationException("message"));

        mockMvc.perform(get("/api/customers/" + Integer.MAX_VALUE))
                .andExpect(status().isBadRequest());

        verify(customerService, times(1)).getOne(ArgumentMatchers.any(CustomerView.class), ArgumentMatchers.anyLong());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void getCustomer() throws Exception {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1l);
        customerDTO.setName("test-customer-name");
        customerDTO.setSurname("test-customer-surname");

        Mockito.when(customerService.getOne(ArgumentMatchers.any(CustomerView.class), ArgumentMatchers.anyLong()) )
                .thenReturn(customerDTO);

        mockMvc.perform(get("/api/customers/" + 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is("test-customer-name")))
                .andExpect(jsonPath("$.surname", is("test-customer-surname")));

        verify(customerService, times(1)).getOne(ArgumentMatchers.any(CustomerView.class), ArgumentMatchers.eq(1l));
        verifyNoMoreInteractions(customerService);

    }

    @Test
    public void getCustomerDefaultViewBasic() throws Exception {

        Mockito.when(customerService.getOne(ArgumentMatchers.any(CustomerView.class), ArgumentMatchers.anyLong()) )
                .thenReturn(new CustomerDTO());

        mockMvc.perform(get("/api/customers/" + 1))
                .andExpect(status().isOk());

        verify(customerService, times(1)).getOne(ArgumentMatchers.eq(CustomerView.BASIC), ArgumentMatchers.eq(1l));
        verifyNoMoreInteractions(customerService);

    }

    @Test
    public void getCustomerWithBasicView() throws Exception {

        Mockito.when(customerService.getOne(ArgumentMatchers.any(CustomerView.class), ArgumentMatchers.anyLong()) )
                .thenReturn(new CustomerDTO());

        mockMvc.perform(get("/api/customers/1?view=BASIC"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).getOne(ArgumentMatchers.eq(CustomerView.BASIC), ArgumentMatchers.eq(1l));
        verifyNoMoreInteractions(customerService);

    }

    @Test
    public void getCustomerWithFullView() throws Exception {

        Mockito.when(customerService.getOne(ArgumentMatchers.any(CustomerView.class), ArgumentMatchers.anyLong()) )
                .thenReturn(new CustomerDTO());

        mockMvc.perform(get("/api/customers/1?view=FULL"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).getOne(ArgumentMatchers.eq(CustomerView.FULL), ArgumentMatchers.eq(1l));
        verifyNoMoreInteractions(customerService);

    }


}
