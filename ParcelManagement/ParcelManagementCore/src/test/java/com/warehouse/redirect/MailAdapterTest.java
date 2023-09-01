package com.warehouse.redirect;

import com.warehouse.mail.domain.service.MailService;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.infrastructure.adapter.secondary.MailAdapter;
import com.warehouse.redirect.infrastructure.adapter.secondary.mapper.NotificationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailAdapterTest {

    @Mock
    private MailService mailService;

    private MailAdapter mailAdapter;

    @Mock
    private NotificationMapper notificationMapper;

    @BeforeEach
    void setup() {
        mailAdapter = new MailAdapter(mailService, notificationMapper);
    }


    @Test
    void shouldSendRedirectNotification() {
        // given
        final RedirectToken redirectToken = new RedirectToken();

        final com.warehouse.mail.domain.vo.Notification mappedNotification =
                new com.warehouse.mail.domain.vo.Notification();

        doReturn(mappedNotification)
                .when(notificationMapper)
                .map(any());
        // when
        mailAdapter.sendRedirectInformation(redirectToken);

        // then
        verify(mailService, times(1)).sendNotification(mappedNotification);
    }
}

