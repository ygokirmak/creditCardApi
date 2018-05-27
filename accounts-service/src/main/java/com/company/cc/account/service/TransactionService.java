package com.company.cc.account.service;

import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.service.dto.AccountDTO;
import com.company.cc.account.service.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {

    public List<TransactionDTO> getTransactions(long accountId) throws ServiceCommunicationException;
}
