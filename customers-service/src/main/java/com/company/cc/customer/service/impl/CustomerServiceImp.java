package com.company.cc.customer.service.impl;

import com.company.cc.customer.domain.Customer;
import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.repository.CustomerRepository;
import com.company.cc.customer.service.AccountService;
import com.company.cc.customer.service.CustomerService;
import com.company.cc.customer.service.TransactionService;
import com.company.cc.customer.service.dto.CustomerDTO;
import com.company.cc.customer.service.enumeration.CustomerView;
import com.company.cc.customer.service.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImp implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    AccountService accountService;
    TransactionService transactionService;

    public CustomerServiceImp(CustomerRepository customerRepository, CustomerMapper customerMapper,
                              AccountService accountService, TransactionService transactionService) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Override
    public CustomerDTO getOne(CustomerView view, long id) throws EntityNotFoundException, ServiceCommunicationException {
        Optional<Customer> optional = customerRepository.findById(id);

        if( !optional.isPresent() ){
            throw new EntityNotFoundException(Customer.class, "id", String.valueOf(id));
        }
        else {
            CustomerDTO customerDTO = customerMapper.toDto(optional.get());

            if( view.equals(CustomerView.FULL) ){
                customerDTO.setAccounts(accountService.getAccounts(id));
                customerDTO.setBalance(transactionService.getTransactionSummary(id).getBalance());
            }

            return customerDTO;
        }
    }

    @Override
    public List<CustomerDTO> getCustomers(CustomerView view) {
        return customerRepository.findAll().stream().map(customerMapper::toDto).collect(Collectors.toList());
    }
}
