package com.company.cc.customer.service;

import com.company.cc.customer.exceptions.EntityNotFoundException;
import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.dto.CustomerDTO;
import com.company.cc.customer.service.enumeration.CustomerView;

import java.util.List;

public interface CustomerService {

    CustomerDTO getOne(CustomerView view, long id) throws EntityNotFoundException, ServiceCommunicationException;

    List<CustomerDTO> getCustomers(CustomerView view);
}
