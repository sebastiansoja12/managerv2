package com.warehouse.shipment;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.infrastructure.adapter.secondary.MailAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapperImpl;

@ExtendWith(MockitoExtension.class)
public class MailAdapterTest {


    @Mock
    private MailPort mailPort;

    private MailAdapter mailAdapter;

    private NotificationMapper notificationMapper;

    @BeforeEach
    void setup() {
        notificationMapper = new NotificationMapperImpl();
        mailAdapter = new MailAdapter(mailPort, notificationMapper);
    }

    @Test
    void shouldSendShipmentNotification() {
        // given
        final Notification notification = new Notification("abc", "abc", "abc");

        final com.warehouse.mail.domain.vo.Notification mappedNotification =
                com.warehouse.mail.domain.vo.Notification.builder().build();

        // when
        mailAdapter.sendShipmentNotification(notification);

        // then
        verify(mailPort, times(1)).sendNotification(mappedNotification);
    }

    @Test
    void shouldSendRerouteNotification() {
        // given
        final Notification notification = new Notification("abc", "abc", "abc");

        final com.warehouse.mail.domain.vo.Notification mappedNotification =
                com.warehouse.mail.domain.vo.Notification.builder().build();

        // when
        mailAdapter.sendRerouteNotification(notification);

        // then
        verify(mailPort, times(1)).sendNotification(mappedNotification);
    }
}
