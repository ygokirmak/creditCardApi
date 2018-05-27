package com.company.cc.account.service;

import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.shared.TransactionDTO;

import java.util.List;

public interface TransactionService {

    public List<TransactionDTO> getTransactions(long accountId) throws ServiceCommunicationException;
}
