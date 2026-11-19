package com.warehouse.shipment;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.api.event.ShipmentPalletReadModelChanged;
import com.warehouse.shipment.api.event.ShipmentReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentPalletReadModelData;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import com.warehouse.shipment.infrastructure.adapter.primary.kafka.ShipmentIntegrationListener;
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
import static org.mockito.Mockito.mock;
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
        final ObjectMapper objectMapper = JsonMapper.builder().configureForJackson2().build();
        final var message = objectMapper.createObjectNode();
        message.set("snapshot", objectMapper.valueToTree(shipment.snapshot()));
        message.put("timestamp", timestamp.toString());

        final ShipmentReadModelChanged event = objectMapper.treeToValue(message, ShipmentReadModelChanged.class);

        assertThat(event.snapshot().shipmentId()).isEqualTo(shipment.getShipmentId());
        assertThat(event.timestamp()).isEqualTo(timestamp);
    }

    @Test
    void shouldSyncPalletTrackingNumberWhenShipmentIsLoadedOnPallet() {
        final ShipmentReadModelSyncPort syncPort = mock(ShipmentReadModelSyncPort.class);
        final ShipmentIntegrationListener listener = new ShipmentIntegrationListener(syncPort);
        final PalletTrackingNumber palletTrackingNumber = new PalletTrackingNumber("PAL000000000001");

        listener.handle(new ShipmentPalletReadModelChanged(
                new ShipmentPalletReadModelData(new ShipmentId(7L), palletTrackingNumber)));

        verify(syncPort).syncPalletTrackingNumber(new ShipmentId(7L), palletTrackingNumber);
    }

    @Test
    void shouldClearPalletTrackingNumberWhenShipmentLeavesPallet() {
        final ShipmentReadModelSyncPort syncPort = mock(ShipmentReadModelSyncPort.class);
        final ShipmentIntegrationListener listener = new ShipmentIntegrationListener(syncPort);

        listener.handle(new ShipmentPalletReadModelChanged(
                new ShipmentPalletReadModelData(new ShipmentId(7L), null)));

        verify(syncPort).syncPalletTrackingNumber(new ShipmentId(7L), null);
    }

    @Test
    void shouldSerializePalletReadModelEventWithShipmentKey() throws Exception {
        final ObjectMapper objectMapper = JsonMapper.builder().configureForJackson2().build();
        final ShipmentPalletReadModelChanged event = new ShipmentPalletReadModelChanged(
                new ShipmentPalletReadModelData(new ShipmentId(7L), new PalletTrackingNumber("PAL000000000001")));

        final ShipmentPalletReadModelChanged deserialized = objectMapper.readValue(
                objectMapper.writeValueAsString(event), ShipmentPalletReadModelChanged.class);

        assertThat(deserialized.payload()).isEqualTo(event.payload());
        assertThat(deserialized.eventKey()).isEqualTo("7");
    }
}
