package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryIntegrationEventServicePort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import com.warehouse.shipment.api.event.ShipmentDeliveryReadModelChanged;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.ORIGIN_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.USER_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ShipmentDeliveryDetailsListenerTest {

    private final DeliveryIntegrationEventServicePort eventServicePort = mock(DeliveryIntegrationEventServicePort.class);
    private final ShipmentDeliveryDetailsListener listener = new ShipmentDeliveryDetailsListener(eventServicePort);

    @Test
    void shouldPublishShipmentDeliveryReadModelChangedWhenShipmentDeliveryChanged() {
        listener.handle(new DeliveryModified(shipmentDelivery(new ShipmentId(10L)).snapshot(), Instant.now()));

        final ArgumentCaptor<ShipmentDeliveryReadModelChanged> event =
                ArgumentCaptor.forClass(ShipmentDeliveryReadModelChanged.class);
        verify(eventServicePort).publishEvent(event.capture());
        assertThat(event.getValue().payload().shipmentId()).isEqualTo(new ShipmentId(10L));
        assertThat(event.getValue().payload().originDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(event.getValue().payload().destinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(event.getValue().eventKey()).isEqualTo("10");
    }

    @Test
    void shouldIgnoreCanceledShipmentDelivery() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.markAsCanceled();

        listener.handle(new DeliveryCanceled(delivery.snapshot(), Instant.now()));

        verify(eventServicePort, never()).publishEvent(any(ShipmentDeliveryReadModelChanged.class));
    }

    @Test
    void shouldIgnorePalletDelivery() {
        listener.handle(new DeliveryModified(Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), null, null, USER_ID)
                .snapshot(), Instant.now()));

        verify(eventServicePort, never()).publishEvent(any(ShipmentDeliveryReadModelChanged.class));
    }
}
