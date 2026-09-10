package com.warehouse.shipment;

import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.application.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReturnCanceledIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentReturnCreatedIntegrationEvent;
import com.warehouse.shipment.application.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.application.listener.ShipmentIntegrationEventListener;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.event.ShipmentCreated;
import com.warehouse.shipment.domain.event.ShipmentDestinationChanged;
import com.warehouse.shipment.domain.event.ShipmentReturnCanceled;
import com.warehouse.shipment.domain.event.ShipmentReturnCreated;
import com.warehouse.shipment.domain.event.ShipmentSent;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShipmentIntegrationEventListenerTest {

    @Mock
    private IntegrationEventPublisher integrationEventPublisher;

    private ShipmentIntegrationEventListener listener;

    @BeforeEach
    void setUp() {
        this.listener = new ShipmentIntegrationEventListener(this.integrationEventPublisher);
    }

    @Test
    void shouldPublishCreatedIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentCreated(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentCreatedIntegrationEvent.class, shipment);
    }

    @Test
    void shouldPublishDestinationChangedIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentDestinationChanged(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentDestinationChangedIntegrationEvent.class, shipment);
    }

    @Test
    void shouldPublishReturnCanceledIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentReturnCanceled(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentReturnCanceledIntegrationEvent.class, shipment);
    }

    @Test
    void shouldPublishStatusChangedIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentSent(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentStatusChangedIntegrationEvent.class, shipment);
    }

    @Test
    void shouldPublishReturnCreatedIntegrationEventWithReturnDetails() {
        final Shipment shipment = DataTestCreator.shipment();
        final DepartmentCode departmentCode = new DepartmentCode("KT2");
        final Instant timestamp = Instant.parse("2026-08-31T06:00:00Z");
        final ArgumentCaptor<IntegrationEvent> eventCaptor = ArgumentCaptor.forClass(IntegrationEvent.class);

        this.listener.handle(new ShipmentReturnCreated(
                shipment.snapshot(),
                ReasonCode.DAMAGED,
                "Package damaged",
                departmentCode,
                timestamp
        ));

        verify(this.integrationEventPublisher).publish(eventCaptor.capture());
        final ShipmentReturnCreatedIntegrationEvent event =
                (ShipmentReturnCreatedIntegrationEvent) eventCaptor.getValue();
        assertEquals(shipment.getShipmentId(), event.getSnapshot().shipmentId());
        assertEquals(timestamp, event.getTimestamp());
        assertEquals(ReasonCode.DAMAGED.name(), event.getReasonCode());
        assertEquals("Package damaged", event.getReason());
        assertEquals(departmentCode, event.getDepartmentCode());
    }

    private void assertPublishedEvent(final Class<? extends IntegrationEvent> expectedType,
                                      final Shipment shipment) {
        final ArgumentCaptor<IntegrationEvent> eventCaptor = ArgumentCaptor.forClass(IntegrationEvent.class);
        verify(this.integrationEventPublisher).publish(eventCaptor.capture());
        final IntegrationEvent event = eventCaptor.getValue();
        assertEquals(expectedType, event.getClass());
        assertEquals(shipment.getShipmentId(), ((ShipmentChangedIntegrationEvent) event).payload().shipmentId());
    }
}
