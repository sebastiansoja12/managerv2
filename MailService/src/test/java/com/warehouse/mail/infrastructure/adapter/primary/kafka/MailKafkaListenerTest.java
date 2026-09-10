package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDeliveredIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentOutForDeliveryIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.warehouse.mail.MailFixture.party;
import static com.warehouse.mail.MailFixture.shipment;
import static com.warehouse.mail.MailFixture.shipmentMailRequestMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MailKafkaListenerTest {

    private final RecordingMailPort mailPort = new RecordingMailPort();
    private final MailKafkaListener listener = new MailKafkaListener(mailPort, shipmentMailRequestMapper());
    private final ShipmentEventData shipment = shipment(ShipmentStatus.SENT,
            party("Jan", "jan@example.com", "PL"), party("Anna", "anna@example.com", "PL"));

    @Test
    void shouldSendShipmentCreatedMails() {
        listener.handle(new ShipmentCreatedIntegrationEvent(shipment, null));

        assertEquals(List.of("shipment.created.sender", "shipment.created.recipient"), mailPort.templateKeys());
    }

    @Test
    void shouldSendStatusChangedMails() {
        listener.handle(new ShipmentStatusChangedIntegrationEvent(shipment));

        assertEquals(List.of("shipment.status.sent.sender", "shipment.status.sent.recipient"), mailPort.templateKeys());
    }

    @Test
    void shouldIgnoreOtherEventsFromSharedShipmentTopic() {
        listener.handle(new ShipmentDestinationChangedIntegrationEvent(shipment, null));

        assertEquals(List.of(), mailPort.templateKeys());
    }

    @Test
    void shouldSendOutForDeliveryMails() {
        listener.handle(new ShipmentOutForDeliveryIntegrationEvent(shipment));

        assertEquals(List.of("shipment.out-for-delivery.sender", "shipment.out-for-delivery.recipient"),
                mailPort.templateKeys());
    }

    @Test
    void shouldSendDeliveredMails() {
        listener.handle(new ShipmentDeliveredIntegrationEvent(shipment));

        assertEquals(List.of("shipment.delivered.sender", "shipment.delivered.recipient"), mailPort.templateKeys());
    }

    private static final class RecordingMailPort implements MailPort {

        private final List<MailRequest> requests = new ArrayList<>();

        @Override
        public void send(final List<MailRequest> mailRequests) {
            this.requests.addAll(mailRequests);
        }

        @Override
        public void send(final MailMessage mailMessage) {
        }

        List<String> templateKeys() {
            return this.requests.stream().map(MailRequest::templateKey).toList();
        }
    }
}
