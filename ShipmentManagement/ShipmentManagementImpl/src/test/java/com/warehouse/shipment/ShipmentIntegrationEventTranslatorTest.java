package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.publication.DomainEventTranslationBridge;
import com.warehouse.eventoutbox.publication.IntegrationEventTranslationRegistry;
import com.warehouse.commonassets.enumeration.CancellationReason;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.shipment.api.event.ShipmentCanceledIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDeliveredIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.PaymentStateDto;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryData;
import com.warehouse.shipment.application.listener.ShipmentIntegrationEventTranslator;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.event.ShipmentCanceled;
import com.warehouse.shipment.domain.event.ShipmentCollected;
import com.warehouse.shipment.domain.event.ShipmentCreated;
import com.warehouse.shipment.domain.event.ShipmentDelivered;
import com.warehouse.shipment.domain.event.ShipmentDestinationChanged;
import com.warehouse.shipment.domain.event.ShipmentNoteAdded;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.event.ShipmentSent;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryPlan;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentEventDataMapper;

class ShipmentIntegrationEventTranslatorTest {

    private final List<IntegrationEvent> published = new ArrayList<>();
    private final Shipment shipment = DataTestCreator.shipment();

    private DomainEventTranslationBridge bridge;

    @BeforeEach
    void setUp() {
        final IntegrationEventTranslationRegistry registry = new IntegrationEventTranslationRegistry();
        registry.postProcessAfterInitialization(
                new ShipmentIntegrationEventTranslator(new ShipmentEventDataMapper()), "shipmentTranslator");
        this.bridge = new DomainEventTranslationBridge(registry, this.published::add);
    }

    @Test
    void shouldPublishCreatedIntegrationEventWithDeliveryData() {
        final ShipmentDeliveryPlan deliveryPlan = new ShipmentDeliveryPlan(new DepartmentId(9L),
                new DepartmentId(10L), PickupMethod.PICKUP_POINT, DeliveryMethod.LOCKER, null, null);

        this.bridge.onDomainEvent(new ShipmentCreated(this.shipment.snapshot(), deliveryPlan, Instant.now()));

        assertPublished(ShipmentCreatedIntegrationEvent.class);
        final ShipmentCreatedIntegrationEvent event = (ShipmentCreatedIntegrationEvent) this.published.getFirst();
        assertEquals(new DepartmentId(9L), event.delivery().originDepartmentId());
        assertEquals(new DepartmentId(10L), event.delivery().destinationDepartmentId());
        assertEquals(ShipmentDeliveryData.PickupMethod.PICKUP_POINT, event.delivery().pickupMethod());
        assertEquals(ShipmentDeliveryData.DeliveryMethod.LOCKER, event.delivery().deliveryMethod());
    }

    @Test
    void shouldPublishCreatedIntegrationEventWithoutDeliveryDataForDerivedShipment() {
        this.bridge.onDomainEvent(new ShipmentCreated(this.shipment.snapshot(), Instant.now()));

        assertPublished(ShipmentCreatedIntegrationEvent.class);
        assertNull(((ShipmentCreatedIntegrationEvent) this.published.getFirst()).delivery());
    }

    @Test
    void shouldPublishOnlyStatusChangedForStatusChangesWithoutDedicatedEvent() {
        this.bridge.onDomainEvent(new ShipmentSent(this.shipment.snapshot(), Instant.now()));
        this.bridge.onDomainEvent(new ShipmentCollected(this.shipment.snapshot(), Instant.now()));

        assertPublished(ShipmentStatusChangedIntegrationEvent.class, ShipmentStatusChangedIntegrationEvent.class);
    }

    @Test
    void shouldPublishSingleStatusChangedWhenShipmentIsReturned() {
        this.bridge.onDomainEvent(new ShipmentReturned(this.shipment.snapshot(), Instant.now()));

        assertPublished(ShipmentStatusChangedIntegrationEvent.class);
    }

    @Test
    void shouldPublishStatusChangedAndDestinationChangedWhenDestinationChanges() {
        this.bridge.onDomainEvent(new ShipmentDestinationChanged(this.shipment.snapshot(), Instant.now()));

        assertPublished(ShipmentStatusChangedIntegrationEvent.class, ShipmentDestinationChangedIntegrationEvent.class);
    }

    @Test
    void shouldPublishStatusChangedAndCanceledWhenShipmentIsCanceled() {
        this.bridge.onDomainEvent(new ShipmentCanceled(this.shipment.snapshot(), Instant.now()));

        assertPublished(ShipmentStatusChangedIntegrationEvent.class, ShipmentCanceledIntegrationEvent.class);
    }

    @Test
    void shouldPublishPreviousStatusAndPaymentStateWhenShipmentIsCanceled() {
        final ShipmentSnapshot previousSnapshot = this.shipment.snapshot();
        this.shipment.markAsCanceledWithoutPolicy(CancellationReason.CUSTOMER_REQUEST, LocalDateTime.now());

        this.bridge.onDomainEvent(new ShipmentCanceled(previousSnapshot, this.shipment.snapshot(), Instant.now()));

        final ShipmentCanceledIntegrationEvent event = (ShipmentCanceledIntegrationEvent) this.published.get(1);
        assertEquals(ShipmentStatus.CREATED, event.previousStatus());
        assertEquals(ShipmentStatus.CANCELED, event.payload().shipmentStatus());
        assertEquals(PaymentStateDto.NOT_REQUIRED, event.payload().paymentState());
    }

    @Test
    void shouldPublishStatusChangedAndDeliveredWhenShipmentIsDelivered() {
        this.bridge.onDomainEvent(new ShipmentDelivered(this.shipment.snapshot(), Instant.now()));

        assertPublished(ShipmentStatusChangedIntegrationEvent.class, ShipmentDeliveredIntegrationEvent.class);
    }

    @Test
    void shouldNotPublishIntegrationEventForNoteAdded() {
        this.bridge.onDomainEvent(new ShipmentNoteAdded(this.shipment.getShipmentId(), false, Instant.now()));

        assertThat(this.published).isEmpty();
    }

    private void assertPublished(final Class<?>... expectedTypes) {
        assertEquals(List.of(expectedTypes), this.published.stream().<Class<?>>map(Object::getClass).toList());
        assertThat(this.published).allSatisfy(event -> assertEquals(this.shipment.getShipmentId(),
                ((ShipmentChangedIntegrationEvent) event).payload().shipmentId()));
    }
}
