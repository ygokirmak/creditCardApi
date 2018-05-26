package com.company.cc.account.service.impl;

import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.TransactionFetchException;
import com.company.cc.account.repository.CustomerRepository;
import com.company.cc.account.service.CustomerService;
import com.company.cc.account.service.dto.CustomerDTO;
import com.company.cc.account.service.mapper.CustomerMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerServiceImp implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    RestTemplate restTemplate;


    public CustomerServiceImp(CustomerRepository customerRepository, CustomerMapper customerMapper,
                              RestTemplate restTemplate) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.restTemplate = restTemplate;
    }


    @Override
    public CustomerDTO getOne(long id) throws EntityNotFoundException, TransactionFetchException {
        return null;
    }

    @Override
    public Page<CustomerDTO> getCustomers(Pageable pageable) {
        return null;
    }
}
