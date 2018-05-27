package com.company.cc.account.service;


import com.company.cc.account.AccountApplication;
import com.company.cc.account.exceptions.ServiceCommunicationException;
import com.company.cc.account.service.dto.AccountDTO;
import com.company.cc.account.service.impl.NotificationServiceImp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
@Transactional
public class NotificationServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    NotificationService notificationService;
    AccountDTO accountDTO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        notificationService = new NotificationServiceImp(rabbitTemplate);

        accountDTO = new AccountDTO();
        accountDTO.setCustomerId(1L);
        accountDTO.setId(1L);
        accountDTO.setInitialCredit(100d);
    }

    @Test
    public void notifyAccountCreation() throws ServiceCommunicationException {

        Mockito.doNothing().when(rabbitTemplate).convertAndSend(eq("account-created"), eq("foo.bar.baz"), any(AccountDTO.class));

        notificationService.accountCreatedNotification(accountDTO);

        verify(rabbitTemplate, times(1)).convertAndSend(eq("account-created"), eq("foo.bar.baz"), eq(accountDTO));

        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    public void failedToNotifyAccountCreation_CommunicationException() throws ServiceCommunicationException {
        Mockito.doThrow(new AmqpException("message")).when(rabbitTemplate)
                .convertAndSend(eq("account-created"), eq("foo.bar.baz"), any(AccountDTO.class));

        assertThatThrownBy(() -> notificationService.accountCreatedNotification(accountDTO))
                .isInstanceOf(ServiceCommunicationException.class);

    }



}
