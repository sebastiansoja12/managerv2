package com.warehouse.mail;


import com.warehouse.mail.domain.port.primary.MailPortImpl;
import com.warehouse.mail.domain.port.secondary.MailServicePort;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.mail.domain.service.MailServiceImpl;
import com.warehouse.mail.domain.vo.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailPortImplTest {


    @Mock
    private MailServicePort mailServicePort;

    private MailPortImpl mailPort;

    @BeforeEach
    void setup() {
        final MailService mailService = new MailServiceImpl(mailServicePort);
        mailPort = new MailPortImpl(mailService);
    }

    @Test
    void shouldSendNotification() {
        // given
        final Notification notification = Notification.builder()
                .body("Test Body")
                .recipient("test@example.com")
                .subject("Test Subject")
                .build();

        doNothing()
                .when(mailServicePort)
                .sendNotification(notification);
        // when
        mailPort.sendNotification(notification);
        // then
        verify(mailServicePort, times(1)).sendNotification(notification);
    }
}
