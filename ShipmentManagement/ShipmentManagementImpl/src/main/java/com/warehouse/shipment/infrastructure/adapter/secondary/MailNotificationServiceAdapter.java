package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationDto;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.application.port.secondary.MailNotificationServicePort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailNotificationServiceAdapter implements MailNotificationServicePort {

    private final NotificationEventPublisher mailEventPublisher;

    private final String shipmentEditUrl;

    public MailNotificationServiceAdapter(final NotificationEventPublisher mailEventPublisher,
                                          final String shipmentEditUrl) {
        this.mailEventPublisher = mailEventPublisher;
        this.shipmentEditUrl = shipmentEditUrl;
    }

    @Override
    public void notifySenderShipmentCreated(final ShipmentSnapshot snapshot) {
        final String trackingNumber = snapshot.trackingNumber().value();
        final NotificationDto notification = new NotificationDto(
                "Przesyłka " + trackingNumber + " została nadana",
                buildShipmentCreatedBody(snapshot),
                snapshot.sender().getEmail());
        try {
            mailEventPublisher.send(new NotificationEvent(notification));
            log.info("Shipment created notification sent for shipment {}", snapshot.shipmentId().getValue());
        } catch (final RuntimeException exception) {
            log.warn("Shipment created notification could not be sent for shipment {}: {}",
                    snapshot.shipmentId().getValue(), exception.getMessage());
        }
    }

    private String buildShipmentCreatedBody(final ShipmentSnapshot snapshot) {
        final Party sender = snapshot.sender();
        final Party recipient = snapshot.recipient();
        final String trackingNumber = snapshot.trackingNumber().value();
        return """
                Dzień dobry,

                Twoja przesyłka została nadana.

                Numer przesyłki: %s

                Nadawca: %s %s, %s, %s %s, %s, tel. %s
                Odbiorca: %s %s, %s, %s %s, %s, tel. %s

                Jeśli dane przesyłki są błędne, możesz je poprawić, dopóki przesyłka nie zostanie odebrana przez kuriera ani umieszczona w paczkomacie:
                %s
                """.formatted(
                trackingNumber,
                sender.getFirstName(), sender.getLastName(), sender.getStreet(), sender.getPostalCode(),
                sender.getCity(), sender.getCountryCode(), sender.getTelephoneNumber(),
                recipient.getFirstName(), recipient.getLastName(), recipient.getStreet(), recipient.getPostalCode(),
                recipient.getCity(), recipient.getCountryCode(), recipient.getTelephoneNumber(),
                shipmentEditUrl + "/" + URLEncoder.encode(trackingNumber, StandardCharsets.UTF_8));
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
