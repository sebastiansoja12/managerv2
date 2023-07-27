package com.warehouse.shipment;

import com.warehouse.mail.domain.service.MailService;
import com.warehouse.shipment.domain.vo.Notification;
import com.warehouse.shipment.infrastructure.adapter.secondary.MailAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailAdapterTest {


    @Mock
    private MailService mailService;

    private MailAdapter mailAdapter;

    private NotificationMapper notificationMapper;

    @BeforeEach
    void setup() {
        notificationMapper = new NotificationMapperImpl();
        mailAdapter = new MailAdapter(mailService, notificationMapper);
    }

    @Test
    void shouldSendNotification() {
        // given
        final Notification notification = new Notification();

        final com.warehouse.mail.domain.vo.Notification mappedNotification =
                new com.warehouse.mail.domain.vo.Notification();

        // when
        mailAdapter.sendShipmentNotification(notification);

        // then
        verify(mailService, times(1)).sendNotification(mappedNotification);
    }
}
