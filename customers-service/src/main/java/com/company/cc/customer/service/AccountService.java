package com.company.cc.customer.service;

import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.dto.AccountDTO;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccounts(long customerId) throws ServiceCommunicationException;
}
