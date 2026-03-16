package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliveryreturn.domain.port.secondary.MailServicePort;
import com.warehouse.deliveryreturn.domain.vo.Shipment;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.MailMapper;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationDto;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailAdapter implements MailServicePort {

    private final MailMapper mapper = getMapper(MailMapper.class);

    private final NotificationEventPublisher notificationEventPublisher;

    @Override
    public void sendNotification(final Shipment shipment) {
        sendEvent(buildEvent(shipment));
    }

    private NotificationEvent buildEvent(final Shipment shipment) {
        return NotificationEvent.builder()
                .notification(new NotificationDto(shipment.getRecipientEmail(), "", shipment.getSenderEmail()))
                .build();
    }

    private void sendEvent(final NotificationEvent notificationEvent) {
        this.notificationEventPublisher.send(notificationEvent);
    }
}
