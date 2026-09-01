package com.warehouse.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.shipment.application.event.ShipmentReadModelChanged;
import com.warehouse.shipment.application.listener.ShipmentReadModelSyncIntegrationEventListener;
import com.warehouse.shipment.domain.event.ShipmentChanged;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShipmentReadModelSyncIntegrationEventListenerTest {

    @Mock
    private IntegrationEventPublisher integrationEventPublisher;

    @Test
    void shouldTranslateShipmentChangeToKeyedReadModelIntegrationEvent() {
        final Shipment shipment = DataTestCreator.shipment();
        final Instant timestamp = Instant.parse("2026-08-31T12:00:00Z");
        final ShipmentReadModelSyncIntegrationEventListener listener =
                new ShipmentReadModelSyncIntegrationEventListener(this.integrationEventPublisher);
        final ArgumentCaptor<ShipmentReadModelChanged> eventCaptor =
                ArgumentCaptor.forClass(ShipmentReadModelChanged.class);

        listener.handle(new ShipmentChanged(shipment.snapshot(), timestamp));

        verify(this.integrationEventPublisher).publish(eventCaptor.capture());
        final ShipmentReadModelChanged event = eventCaptor.getValue();
        assertThat(event.snapshot().shipmentId()).isEqualTo(shipment.getShipmentId());
        assertThat(event.timestamp()).isEqualTo(timestamp);
        assertThat(event.eventKey()).isEqualTo(String.valueOf(shipment.getShipmentId().getValue()));
    }

    @Test
    void shouldDeserializeLegacyMessageWithFullShipmentSnapshot() throws Exception {
        final Shipment shipment = DataTestCreator.shipment();
        final Instant timestamp = Instant.parse("2026-08-31T12:00:00Z");
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final var message = objectMapper.createObjectNode();
        message.set("snapshot", objectMapper.valueToTree(shipment.snapshot()));
        message.put("timestamp", timestamp.toString());

        final ShipmentReadModelChanged event = objectMapper.treeToValue(message, ShipmentReadModelChanged.class);

        assertThat(event.snapshot().shipmentId()).isEqualTo(shipment.getShipmentId());
        assertThat(event.timestamp()).isEqualTo(timestamp);
    }
}
