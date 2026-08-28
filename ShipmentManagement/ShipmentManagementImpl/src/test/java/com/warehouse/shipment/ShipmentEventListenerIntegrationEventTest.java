package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.ObjectProvider;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.kafka.application.IntegrationEventOutboxWriter;
import com.warehouse.shipment.application.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.application.listener.ShipmentEventListener;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.event.ShipmentCreatedEvent;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

class ShipmentEventListenerIntegrationEventTest {

    @Test
    void shouldWriteCreatedIntegrationEventToShipmentOutbox() {
        final ShipmentPort shipmentPort = mock(ShipmentPort.class);
        final PathFinderServicePort pathFinderServicePort = mock(PathFinderServicePort.class);
        final IntegrationEventOutboxWriter outboxWriter = mock(IntegrationEventOutboxWriter.class);
        @SuppressWarnings("unchecked")
        final ObjectProvider<IntegrationEventOutboxWriter> writerProvider = mock(ObjectProvider.class);
        when(writerProvider.getIfAvailable()).thenReturn(outboxWriter);
        final ShipmentEventListener listener = new ShipmentEventListener(
                shipmentPort, pathFinderServicePort, writerProvider, "shipment.events");
        final Instant occurredAt = Instant.parse("2026-08-11T10:15:30Z");
        final ShipmentSnapshot domainSnapshot = new ShipmentSnapshot(
                new ShipmentId(123L),
                null,
                null,
                null,
                null,
                null,
                ShipmentStatus.CREATED,
                null,
                null,
                null,
                null,
                null,
                false,
                null,
                false,
                null,
                null,
                null,
                null,
                new TrackingNumber("TRACKING-123"),
                null
        );
        final ArgumentCaptor<ShipmentCreatedIntegrationEvent> eventCaptor =
                ArgumentCaptor.forClass(ShipmentCreatedIntegrationEvent.class);

        listener.handle(new ShipmentCreatedEvent(domainSnapshot, occurredAt));

        verify(outboxWriter).write(
                eq("shipment.events"),
                eq("123"),
                any(),
                eq("shipment.created"),
                eq(1),
                eq(occurredAt),
                eventCaptor.capture()
        );
        final ShipmentCreatedIntegrationEvent integrationEvent = eventCaptor.getValue();
        assertThat(integrationEvent.payload()).isEqualTo(
                com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot.from(domainSnapshot));
        assertThat(integrationEvent.eventId()).isNotNull();
    }
}
