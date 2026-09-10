package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryReadModelPort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DeliveryReadModelListenerTest {

    @Test
    void shouldSyncReadModelWhenDeliveryChanged() {
        final DeliveryReadModelPort deliveryReadModelPort = mock(DeliveryReadModelPort.class);
        final DeliveryReadModelListener listener = new DeliveryReadModelListener(deliveryReadModelPort);
        final DeliverySnapshot snapshot = shipmentDelivery(new ShipmentId(10L)).snapshot();

        listener.handle(new DeliveryModified(snapshot, Instant.now()));

        verify(deliveryReadModelPort).sync(snapshot);
    }
}
