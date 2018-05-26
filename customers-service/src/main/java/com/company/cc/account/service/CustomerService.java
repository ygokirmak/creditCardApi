package com.company.cc.account.service;

import com.company.cc.account.exceptions.EntityNotFoundException;
import com.company.cc.account.exceptions.TransactionFetchException;
import com.company.cc.account.service.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerDTO getOne(long id) throws EntityNotFoundException, TransactionFetchException;

    Page<CustomerDTO> getCustomers(Pageable pageable);
}
