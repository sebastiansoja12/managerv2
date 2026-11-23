package com.warehouse.logisticsdelivery.application.listener;

import java.util.ArrayList;
import java.util.List;

import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.publication.DomainEventTranslationBridge;
import com.warehouse.eventoutbox.publication.IntegrationEventTranslationRegistry;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.api.event.DeliveryChangedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCompletedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCreatedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.snapshot.DeliveryEventData;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DeliveryEventDataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.ORIGIN_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.USER_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;

class DeliveryIntegrationEventTranslatorTest {

    private final List<IntegrationEvent> published = new ArrayList<>();
    private DomainEventTranslationBridge bridge;

    @BeforeEach
    void setUp() {
        final IntegrationEventTranslationRegistry registry = new IntegrationEventTranslationRegistry();
        registry.postProcessAfterInitialization(new DeliveryIntegrationEventTranslator(new DeliveryEventDataMapper()),
                "deliveryIntegrationEventTranslator");
        this.bridge = new DomainEventTranslationBridge(registry, this.published::add);
    }

    @Test
    void shouldPublishCreatedIntegrationEventWithRouteData() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));

        this.bridge.onDomainEvent(new DeliveryCreated(delivery.snapshot(), Instant.now()));

        final DeliveryEventData payload = publishedEvent(DeliveryCreatedIntegrationEvent.class).payload();
        assertThat(payload.deliveryId()).isEqualTo(delivery.getDeliveryId());
        assertThat(payload.shipmentId()).isEqualTo(new ShipmentId(10L));
        assertThat(payload.targetType()).isEqualTo("SHIPMENT");
        assertThat(payload.originDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(payload.destinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(payload.pickupMethod()).isEqualTo("PICKUP_POINT");
        assertThat(payload.deliveryMethod()).isEqualTo("LOCKER");
        assertThat(payload.lastStep().stepNumber()).isEqualTo(1);
    }

    @Test
    void shouldPublishCompletedIntegrationEventWithFinalStep() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.complete(null, USER_ID, null);

        this.bridge.onDomainEvent(new DeliveryCompleted(delivery.snapshot(), Instant.now()));

        final DeliveryEventData payload = publishedEvent(DeliveryCompletedIntegrationEvent.class).payload();
        assertThat(payload.lifecycleStatus()).isEqualTo("COMPLETED");
        assertThat(payload.lastStep().stepNumber()).isEqualTo(2);
        assertThat(payload.lastStep().outcome()).isEqualTo("SUCCEEDED");
    }

    private <T extends DeliveryChangedIntegrationEvent> T publishedEvent(final Class<T> eventType) {
        assertThat(this.published).hasSize(1);
        assertThat(this.published.getFirst()).isInstanceOf(eventType);
        return eventType.cast(this.published.getFirst());
    }
}
