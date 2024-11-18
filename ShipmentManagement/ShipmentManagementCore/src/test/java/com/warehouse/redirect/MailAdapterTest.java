package com.warehouse.redirect;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.redirect.infrastructure.adapter.secondary.properties.RedirectTokenProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.infrastructure.adapter.secondary.MailAdapter;
import com.warehouse.redirect.infrastructure.adapter.secondary.mapper.NotificationMapper;

@ExtendWith(MockitoExtension.class)
public class MailAdapterTest {

    @Mock
    private MailPort mailPort;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private RedirectTokenProperties properties;

    private MailAdapter mailAdapter;

    @BeforeEach
    void setup() {
        mailAdapter = new MailAdapter(mailPort, notificationMapper, properties);
    }


    @Test
    void shouldSendRedirectNotification() {
        // given
        final RedirectToken redirectToken = mock(RedirectToken.class);

        final com.warehouse.mail.domain.vo.Notification mappedNotification = Notification.builder().build();

        doReturn(mappedNotification)
                .when(notificationMapper)
                .map(any());
        // when
        mailAdapter.sendRedirectInformation(redirectToken);

        // then
        verify(mailPort, times(1)).sendNotification(mappedNotification);
    }
}

