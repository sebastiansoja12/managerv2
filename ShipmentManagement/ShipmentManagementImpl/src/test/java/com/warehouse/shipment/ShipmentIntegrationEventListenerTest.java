package com.warehouse.shipment;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.application.listener.ShipmentIntegrationEventListener;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentIntegrationEventServiceAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentEventDataMapper;
import com.warehouse.shipment.domain.event.ShipmentCreated;
import com.warehouse.shipment.domain.event.ShipmentDestinationChanged;
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
        this.listener = new ShipmentIntegrationEventListener(
                new ShipmentIntegrationEventServiceAdapter(this.integrationEventPublisher),
                new ShipmentEventDataMapper());
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
    void shouldPublishStatusChangedIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentSent(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentStatusChangedIntegrationEvent.class, shipment);
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
