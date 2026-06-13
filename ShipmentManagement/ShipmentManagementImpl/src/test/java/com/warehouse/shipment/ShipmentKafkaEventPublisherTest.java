package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.shipment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.event.ShipmentCreatedEvent;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentReturnCreated;
import com.warehouse.shipment.infrastructure.adapter.secondary.kafka.ShipmentKafkaEventPublisher;

class ShipmentKafkaEventPublisherTest {

    private final KafkaTemplateClient kafkaTemplateClient = mock(KafkaTemplateClient.class);
    private final ShipmentKafkaEventPublisher publisher = new ShipmentKafkaEventPublisher(kafkaTemplateClient, "shipment.events");

    @Test
    void shouldPublishShipmentReturnCreatedAsDomainEvent() {
        final DepartmentCode departmentCode = new DepartmentCode("KT1");
        final ShipmentReturnCreated event = new ShipmentReturnCreated(
                shipment().snapshot(),
                ReasonCode.DAMAGED,
                "Damaged package",
                departmentCode,
                Instant.parse("2026-08-25T10:15:30Z")
        );
        final ArgumentCaptor<ShipmentEvent> eventCaptor = ArgumentCaptor.forClass(ShipmentEvent.class);
        final ArgumentCaptor<Map<String, String>> headersCaptor = ArgumentCaptor.forClass(Map.class);

        publisher.publish(event);

        verify(kafkaTemplateClient).publish(eq("shipment.events"), eq("1"), eventCaptor.capture(), headersCaptor.capture());
        assertSame(event, eventCaptor.getValue());
        assertEquals("ShipmentReturnCreated", headersCaptor.getValue().get(KafkaEventHeaders.EVENT_TYPE));
    }

    @Test
    void shouldPublishRegularShipmentEventAsDomainEvent() {
        final ShipmentCreatedEvent event = new ShipmentCreatedEvent(
                shipment().snapshot(),
                Instant.parse("2026-08-25T10:15:30Z")
        );
        final ArgumentCaptor<ShipmentEvent> eventCaptor = ArgumentCaptor.forClass(ShipmentEvent.class);
        final ArgumentCaptor<Map<String, String>> headersCaptor = ArgumentCaptor.forClass(Map.class);

        publisher.publish(event);

        verify(kafkaTemplateClient).publish(eq("shipment.events"), eq("1"), eventCaptor.capture(), headersCaptor.capture());
        assertSame(event, eventCaptor.getValue());
        assertEquals("ShipmentCreatedEvent", headersCaptor.getValue().get(KafkaEventHeaders.EVENT_TYPE));
    }
}
