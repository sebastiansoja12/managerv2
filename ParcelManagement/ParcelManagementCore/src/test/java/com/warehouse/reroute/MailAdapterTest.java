package com.warehouse.reroute;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.mail.domain.service.MailService;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.infrastructure.adapter.secondary.MailAdapter;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.NotificationMapper;

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
    void shouldSendRerouteNotification() {
        // given
        final RerouteToken rerouteToken = new RerouteToken();

        final com.warehouse.mail.domain.vo.Notification mappedNotification =
                new com.warehouse.mail.domain.vo.Notification();

        doReturn(mappedNotification)
                .when(notificationMapper)
                        .map(any());
        // when
        mailAdapter.sendReroutingInformation(rerouteToken);

        // then
        verify(mailService, times(1)).sendNotification(mappedNotification);
    }
}
