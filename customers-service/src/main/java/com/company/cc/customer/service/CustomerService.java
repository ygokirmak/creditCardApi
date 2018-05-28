package com.company.cc.customer.service;

import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO getOne(long id) throws EntityNotFoundException, ServiceCommunicationException;

    List<CustomerDTO> getCustomers();
}
