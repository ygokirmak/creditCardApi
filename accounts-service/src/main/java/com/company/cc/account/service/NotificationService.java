package com.company.cc.account.service;

import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.service.dto.AccountDTO;

public interface NotificationService {

    void accountCreatedNotification(AccountDTO accountDTO) throws ServiceCommunicationException;
}
