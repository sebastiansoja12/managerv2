package com.warehouse.logisticsdelivery.application.listener;

import java.util.ArrayList;
import java.util.List;

import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.publication.DomainEventTranslationBridge;
import com.warehouse.eventoutbox.publication.IntegrationEventTranslationRegistry;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.event.DeliveryReadModelRebuilt;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import com.warehouse.shipment.api.event.ShipmentDeliveryReadModelChanged;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.ORIGIN_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.USER_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;

class ShipmentDeliveryDetailsTranslatorTest {

    private final List<IntegrationEvent> published = new ArrayList<>();
    private final DomainEventTranslationBridge bridge = bridge();

    @Test
    void shouldPublishShipmentDeliveryReadModelChangedWhenShipmentDeliveryChanged() {
        bridge.onDomainEvent(new DeliveryModified(shipmentDelivery(new ShipmentId(10L)).snapshot(), Instant.now()));

        final ShipmentDeliveryReadModelChanged event = publishedEvent();
        assertThat(event.payload().shipmentId()).isEqualTo(new ShipmentId(10L));
        assertThat(event.payload().originDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(event.payload().destinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(event.payload().currentDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(event.eventKey()).isEqualTo("10");
    }

    @Test
    void shouldPublishShipmentDeliveryReadModelChangedWhenDeliveryReadModelRebuilt() {
        bridge.onDomainEvent(new DeliveryReadModelRebuilt(shipmentDelivery(new ShipmentId(11L)).snapshot(), Instant.now()));

        final ShipmentDeliveryReadModelChanged event = publishedEvent();
        assertThat(event.payload().shipmentId()).isEqualTo(new ShipmentId(11L));
        assertThat(event.payload().destinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
    }

    @Test
    void shouldIgnoreCanceledShipmentDelivery() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.markAsCanceled();

        bridge.onDomainEvent(new DeliveryCanceled(delivery.snapshot(), Instant.now()));

        assertThat(this.published).isEmpty();
    }

    @Test
    void shouldIgnorePalletDelivery() {
        bridge.onDomainEvent(new DeliveryModified(Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), null, null, USER_ID)
                .snapshot(), Instant.now()));

        assertThat(this.published).isEmpty();
    }

    private ShipmentDeliveryReadModelChanged publishedEvent() {
        assertThat(this.published).hasSize(1);
        return (ShipmentDeliveryReadModelChanged) this.published.getFirst();
    }

    private DomainEventTranslationBridge bridge() {
        final IntegrationEventTranslationRegistry registry = new IntegrationEventTranslationRegistry();
        registry.postProcessAfterInitialization(new ShipmentDeliveryDetailsTranslator(),
                "shipmentDeliveryDetailsTranslator");
        return new DomainEventTranslationBridge(registry, this.published::add);
    }
}
