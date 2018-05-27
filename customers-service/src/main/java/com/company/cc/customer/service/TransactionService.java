package com.company.cc.customer.service;

import com.company.cc.customer.exceptions.ServiceCommunicationException;
import com.company.cc.customer.service.dto.TransactionSummaryDTO;

public interface TransactionService {

    TransactionSummaryDTO getTransactionSummary(long customerId) throws ServiceCommunicationException;
}
