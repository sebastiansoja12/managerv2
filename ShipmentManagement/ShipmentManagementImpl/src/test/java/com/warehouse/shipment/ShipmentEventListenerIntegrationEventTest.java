package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.event.domain.port.IntegrationEventPublisher;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.application.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReturnCreatedIntegrationEvent;
import com.warehouse.shipment.application.listener.ShipmentEventListener;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.event.ShipmentCreatedEvent;
import com.warehouse.shipment.domain.event.ShipmentReturnCreated;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

class ShipmentEventListenerIntegrationEventTest {

    @Test
    void shouldPublishCreatedIntegrationEvent() {
        final ShipmentPort shipmentPort = mock(ShipmentPort.class);
        final PathFinderServicePort pathFinderServicePort = mock(PathFinderServicePort.class);
        final IntegrationEventPublisher integrationEventPublisher = mock(IntegrationEventPublisher.class);
        final ShipmentEventListener listener = new ShipmentEventListener(
                shipmentPort, pathFinderServicePort, integrationEventPublisher);
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
        final ArgumentCaptor<ShipmentChangedIntegrationEvent> eventCaptor =
                ArgumentCaptor.forClass(ShipmentChangedIntegrationEvent.class);

        listener.handle(new ShipmentCreatedEvent(domainSnapshot, occurredAt));

        verify(integrationEventPublisher).publish(eventCaptor.capture());
        final ShipmentChangedIntegrationEvent integrationEvent = eventCaptor.getValue();
        assertThat(integrationEvent.payload()).isEqualTo(
                com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot.from(domainSnapshot));
    }

    @Test
    void shouldPublishShipmentReturnCreatedIntegrationEvent() {
        final ShipmentPort shipmentPort = mock(ShipmentPort.class);
        final PathFinderServicePort pathFinderServicePort = mock(PathFinderServicePort.class);
        final IntegrationEventPublisher integrationEventPublisher = mock(IntegrationEventPublisher.class);
        final ShipmentEventListener listener = new ShipmentEventListener(
                shipmentPort, pathFinderServicePort, integrationEventPublisher);
        final Instant occurredAt = Instant.parse("2026-08-11T10:15:30Z");
        final DepartmentCode departmentCode = new DepartmentCode("WRO");
        final ShipmentSnapshot domainSnapshot = new ShipmentSnapshot(
                new ShipmentId(123L),
                null,
                null,
                null,
                null,
                null,
                ShipmentStatus.RETURN,
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
        final ArgumentCaptor<ShipmentReturnCreatedIntegrationEvent> eventCaptor =
                ArgumentCaptor.forClass(ShipmentReturnCreatedIntegrationEvent.class);

        listener.handle(new ShipmentReturnCreated(
                domainSnapshot,
                ReasonCode.NO_LONGER_NEEDED,
                "Customer changed their mind",
                departmentCode,
                occurredAt
        ));

        verify(integrationEventPublisher).publish(eventCaptor.capture());
        final ShipmentReturnCreatedIntegrationEvent integrationEvent = eventCaptor.getValue();
        assertThat(integrationEvent.getSnapshot()).isEqualTo(
                com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot.from(domainSnapshot));
        assertThat(integrationEvent.getTimestamp()).isEqualTo(occurredAt);
        assertThat(integrationEvent.getReasonCode()).isEqualTo("NO_LONGER_NEEDED");
        assertThat(integrationEvent.getReason()).isEqualTo("Customer changed their mind");
        assertThat(integrationEvent.getDepartmentCode()).isEqualTo(departmentCode);
    }
}
