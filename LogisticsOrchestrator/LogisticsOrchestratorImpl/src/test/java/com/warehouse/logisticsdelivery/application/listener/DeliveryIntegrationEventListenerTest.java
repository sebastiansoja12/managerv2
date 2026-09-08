package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.api.event.DeliveryChangedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCompletedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCreatedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.snapshot.DeliveryEventData;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryIntegrationEventServicePort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DeliveryEventDataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.ORIGIN_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.USER_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DeliveryIntegrationEventListenerTest {

    private DeliveryIntegrationEventServicePort deliveryIntegrationEventServicePort;
    private DeliveryIntegrationEventListener listener;

    @BeforeEach
    void setUp() {
        this.deliveryIntegrationEventServicePort = mock(DeliveryIntegrationEventServicePort.class);
        this.listener = new DeliveryIntegrationEventListener(this.deliveryIntegrationEventServicePort,
                new DeliveryEventDataMapper());
    }

    @Test
    void shouldPublishCreatedIntegrationEventWithRouteData() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));

        this.listener.handle(new DeliveryCreated(delivery.snapshot(), Instant.now()));

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

        this.listener.handle(new DeliveryCompleted(delivery.snapshot(), Instant.now()));

        final DeliveryEventData payload = publishedEvent(DeliveryCompletedIntegrationEvent.class).payload();
        assertThat(payload.lifecycleStatus()).isEqualTo("COMPLETED");
        assertThat(payload.lastStep().stepNumber()).isEqualTo(2);
        assertThat(payload.lastStep().outcome()).isEqualTo("SUCCEEDED");
    }

    private <T extends DeliveryChangedIntegrationEvent> T publishedEvent(final Class<T> eventType) {
        final ArgumentCaptor<DeliveryChangedIntegrationEvent> event =
                ArgumentCaptor.forClass(DeliveryChangedIntegrationEvent.class);
        verify(this.deliveryIntegrationEventServicePort).publishEvent(event.capture());
        assertThat(event.getValue()).isInstanceOf(eventType);
        return eventType.cast(event.getValue());
    }
}
