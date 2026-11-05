package com.warehouse.mail.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.mail.application.port.primary.MailPort;
import com.warehouse.mail.domain.model.MailMessage;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.vo.ShipmentNotificationPolicy;
import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDeliveredIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentOutForDeliveryIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.warehouse.mail.MailFixture.party;
import static com.warehouse.mail.MailFixture.shipment;
import static com.warehouse.mail.MailFixture.shipmentMailRequestMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MailKafkaListenerTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(3L);

    private final RecordingMailPort mailPort = new RecordingMailPort();
    private final Map<OperatorId, ShipmentNotificationPolicy> policies = new HashMap<>();
    private final MailKafkaListener listener = new MailKafkaListener(mailPort, shipmentMailRequestMapper(),
            operatorId -> policies.getOrDefault(operatorId, ShipmentNotificationPolicy.unrestricted()));
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
        listener.handle(new ShipmentDestinationChangedIntegrationEvent(shipment));

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

    @Test
    void shouldSkipRecipientMailsDisabledByOperatorConfiguration() {
        policies.put(OPERATOR_ID, new ShipmentNotificationPolicy(false, false, false));

        listener.handle(operatorEvent(new ShipmentCreatedIntegrationEvent(shipment, null)));
        listener.handle(operatorEvent(new ShipmentOutForDeliveryIntegrationEvent(shipment)));
        listener.handle(operatorEvent(new ShipmentDeliveredIntegrationEvent(shipment)));

        assertEquals(List.of("shipment.created.sender", "shipment.out-for-delivery.sender",
                "shipment.delivered.sender"), mailPort.templateKeys());
    }

    @Test
    void shouldApplyOperatorConfigurationPerMilestone() {
        policies.put(OPERATOR_ID, new ShipmentNotificationPolicy(false, true, false));

        listener.handle(operatorEvent(new ShipmentCreatedIntegrationEvent(shipment, null)));
        listener.handle(operatorEvent(new ShipmentOutForDeliveryIntegrationEvent(shipment)));

        assertEquals(List.of("shipment.created.sender", "shipment.out-for-delivery.sender",
                "shipment.out-for-delivery.recipient"), mailPort.templateKeys());
    }

    private static <T extends ShipmentChangedIntegrationEvent> T operatorEvent(final T event) {
        event.assignOperatorId(OPERATOR_ID);
        return event;
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
