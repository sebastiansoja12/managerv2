package com.warehouse.shipment.application.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.warehouse.shipment.application.port.secondary.MailNotificationServicePort;
import com.warehouse.shipment.domain.event.ShipmentCreated;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

@Component
public class ShipmentCreatedNotificationListener {

    private final MailNotificationServicePort mailNotificationServicePort;

    public ShipmentCreatedNotificationListener(final MailNotificationServicePort mailNotificationServicePort) {
        this.mailNotificationServicePort = mailNotificationServicePort;
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handle(final ShipmentCreated event) {
        final ShipmentSnapshot snapshot = event.getSnapshot();
        if (snapshot.shipmentRelatedId() != null || snapshot.sender() == null
                || snapshot.sender().getEmail() == null || snapshot.sender().getEmail().isBlank()) {
            return;
        }
        mailNotificationServicePort.notifySenderShipmentCreated(snapshot);
    }
}
