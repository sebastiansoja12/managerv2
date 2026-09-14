package com.warehouse.shipment;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.shipment.api.event.ShipmentCanceledIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDeliveredIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryData;
import com.warehouse.shipment.application.listener.ShipmentIntegrationEventListener;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentIntegrationEventServiceAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentEventDataMapper;
import com.warehouse.shipment.domain.event.ShipmentCanceled;
import com.warehouse.shipment.domain.event.ShipmentCreated;
import com.warehouse.shipment.domain.event.ShipmentDelivered;
import com.warehouse.shipment.domain.event.ShipmentDestinationChanged;
import com.warehouse.shipment.domain.event.ShipmentSent;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        final ShipmentDeliveryPlan deliveryPlan = new ShipmentDeliveryPlan(new DepartmentId(9L),
                new DepartmentId(10L), PickupMethod.PICKUP_POINT, DeliveryMethod.LOCKER, null, null);

        this.listener.handle(new ShipmentCreated(shipment.snapshot(), deliveryPlan, Instant.now()));

        final ShipmentCreatedIntegrationEvent event = (ShipmentCreatedIntegrationEvent)
                assertPublishedEvent(ShipmentCreatedIntegrationEvent.class, shipment);
        assertEquals(new DepartmentId(9L), event.delivery().originDepartmentId());
        assertEquals(new DepartmentId(10L), event.delivery().destinationDepartmentId());
        assertEquals(ShipmentDeliveryData.PickupMethod.PICKUP_POINT, event.delivery().pickupMethod());
        assertEquals(ShipmentDeliveryData.DeliveryMethod.LOCKER, event.delivery().deliveryMethod());
    }

    @Test
    void shouldPublishCreatedIntegrationEventWithoutDeliveryDataForDerivedShipment() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentCreated(shipment.snapshot(), Instant.now()));

        final ShipmentCreatedIntegrationEvent event = (ShipmentCreatedIntegrationEvent)
                assertPublishedEvent(ShipmentCreatedIntegrationEvent.class, shipment);
        assertNull(event.delivery());
    }

    @Test
    void shouldPublishDestinationChangedIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentDestinationChanged(shipment.snapshot(), new DepartmentId(11L), Instant.now()));

        final ShipmentDestinationChangedIntegrationEvent event = (ShipmentDestinationChangedIntegrationEvent)
                assertPublishedEvent(ShipmentDestinationChangedIntegrationEvent.class, shipment);
        assertEquals(new DepartmentId(11L), event.destinationDepartmentId());
    }

    @Test
    void shouldPublishStatusChangedIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentSent(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentStatusChangedIntegrationEvent.class, shipment);
    }

    @Test
    void shouldPublishDeliveredIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentDelivered(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentDeliveredIntegrationEvent.class, shipment);
    }

    @Test
    void shouldPublishCanceledIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();

        this.listener.handle(new ShipmentCanceled(shipment.snapshot(), Instant.now()));

        assertPublishedEvent(ShipmentCanceledIntegrationEvent.class, shipment);
    }

    private IntegrationEvent assertPublishedEvent(final Class<? extends IntegrationEvent> expectedType,
                                                  final Shipment shipment) {
        final ArgumentCaptor<IntegrationEvent> eventCaptor = ArgumentCaptor.forClass(IntegrationEvent.class);
        verify(this.integrationEventPublisher).publish(eventCaptor.capture());
        final IntegrationEvent event = eventCaptor.getValue();
        assertEquals(expectedType, event.getClass());
        assertEquals(shipment.getShipmentId(), ((ShipmentChangedIntegrationEvent) event).payload().shipmentId());
        return event;
    }
}
