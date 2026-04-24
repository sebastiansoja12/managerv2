package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationDto;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.MailNotificationServicePort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailNotificationServiceAdapter implements MailNotificationServicePort {

    private final NotificationEventPublisher mailEventPublisher;

    public MailNotificationServiceAdapter(final NotificationEventPublisher mailEventPublisher) {
        this.mailEventPublisher = mailEventPublisher;
    }

    @Override
    public Result<Void, ErrorCode> notifyRecipient(final DeliveryStatus deliveryStatus, final Shipment shipment) {
        final NotificationDto notification = getNotification(deliveryStatus, shipment);

        return Result.success();
    }

    private NotificationDto getNotification(final DeliveryStatus deliveryStatus, final Shipment shipment) {
        return switch (deliveryStatus) {
            case DELIVERED -> new NotificationDto("", "", "");
            default -> null;
        };
    }
}
