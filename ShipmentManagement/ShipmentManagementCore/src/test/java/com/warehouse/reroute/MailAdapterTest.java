package com.warehouse.reroute;

import static org.mockito.Mockito.*;

import com.warehouse.mail.domain.vo.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.infrastructure.adapter.secondary.MailAdapter;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.NotificationMapper;

@ExtendWith(MockitoExtension.class)
public class MailAdapterTest {

    @Mock
    private MailPort mailPort;

    private MailAdapter mailAdapter;

    @Mock
    private NotificationMapper notificationMapper;

    @BeforeEach
    void setup() {
        mailAdapter = new MailAdapter(mailPort, notificationMapper);
    }


    @Test
    void shouldSendRerouteNotification() {
        // given
        final RerouteToken rerouteToken = RerouteToken.builder()
                .token(12345)
                .build();

        final com.warehouse.mail.domain.vo.Notification mappedNotification = Notification.builder().build();

        doReturn(mappedNotification)
                .when(notificationMapper)
                        .map(any());
        // when
        mailAdapter.sendReroutingInformation(rerouteToken);

        // then
        verify(mailPort, times(1)).sendNotification(mappedNotification);
    }
}
