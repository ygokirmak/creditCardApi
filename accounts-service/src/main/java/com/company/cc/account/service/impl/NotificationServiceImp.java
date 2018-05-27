package com.company.cc.account.service.impl;

import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.service.NotificationService;
import com.company.cc.account.service.dto.AccountDTO;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImp implements NotificationService {
    RabbitTemplate rabbitTemplate;

    public NotificationServiceImp(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void accountCreatedNotification(AccountDTO accountDTO) throws ServiceCommunicationException {
        try {
            rabbitTemplate.convertAndSend("account-created", "foo.bar.baz", accountDTO);
        }catch (AmqpException ex){
            throw new ServiceCommunicationException(ex.getMessage());
        }
    }
}
