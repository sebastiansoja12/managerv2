package com.warehouse.pallet.infrastructure.adapter.primary.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.PalletLoadedRequest;
import com.warehouse.pallet.domain.port.primary.PalletPort;
import com.warehouse.pallet.infrastructure.adapter.primary.kafka.event.PalletLoadedIntegrationEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class PalletLoadedKafkaListenerTest {

    private static final String MESSAGE = """
            {
              "payload": {"palletId": "p1", "trackingNumber": "PAL1", "shipmentIds": [1, 2]},
              "eventId": "6f1c1d0e-1111-2222-3333-444455556666",
              "eventType": "pallet.loaded",
              "version": 1,
              "occurredAt": "2026-10-17T10:00:00Z",
              "operatorId": {"value": 1},
              "userId": {"value": 2},
              "departmentId": null
            }
            """;

    @Test
    void shouldDeserializeMonolithEventAndMarkPalletLoaded() throws Exception {
        final PalletPort palletPort = mock(PalletPort.class);
        final PalletLoadedKafkaListener listener = new PalletLoadedKafkaListener(palletPort);
        final PalletLoadedIntegrationEvent event = new ObjectMapper()
                .readValue(MESSAGE, PalletLoadedIntegrationEvent.class);

        listener.handle(event);

        assertThat(event.payload().shipmentIds()).containsExactly(1L, 2L);
        verify(palletPort).handlePalletLoaded(
                new PalletLoadedRequest("6f1c1d0e-1111-2222-3333-444455556666", new PalletId("p1")));
    }

    @Test
    void shouldIgnoreEventWithoutPallet() {
        final PalletPort palletPort = mock(PalletPort.class);

        new PalletLoadedKafkaListener(palletPort).handle(new PalletLoadedIntegrationEvent("e1", null));

        verify(palletPort, never()).handlePalletLoaded(any());
    }
}
