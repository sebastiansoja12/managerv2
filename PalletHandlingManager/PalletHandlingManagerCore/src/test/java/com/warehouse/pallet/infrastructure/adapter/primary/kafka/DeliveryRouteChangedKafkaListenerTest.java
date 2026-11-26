package com.warehouse.pallet.infrastructure.adapter.primary.kafka;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.PalletRouteChangedRequest;
import com.warehouse.pallet.domain.port.primary.PalletPort;
import com.warehouse.pallet.infrastructure.adapter.primary.kafka.event.DeliveryChangedIntegrationEvent;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class DeliveryRouteChangedKafkaListenerTest {

    private static final String MESSAGE = """
            {
              "payload": {
                "deliveryId": {"id": "d1"},
                "targetType": "%s",
                "targetId": "p1",
                "shipmentId": null,
                "lifecycleStatus": "ACTIVE",
                "originDepartmentId": {"value": 20},
                "destinationDepartmentId": {"value": 30},
                "lastStep": null
              },
              "eventId": "6f1c1d0e-1111-2222-3333-444455556666",
              "eventType": "%s",
              "version": 1,
              "occurredAt": "2026-11-14T10:00:00Z",
              "operatorId": {"value": 1},
              "userId": {"value": 2},
              "departmentId": null
            }
            """;

    @Test
    void shouldChangePalletDepartmentsWhenPalletDeliveryRouteIsModified() throws Exception {
        final PalletPort palletPort = mock(PalletPort.class);
        final DeliveryRouteChangedKafkaListener listener = new DeliveryRouteChangedKafkaListener(palletPort);

        listener.handle(event("PALLET", "delivery.modified"));

        verify(palletPort).handleDeliveryRouteChanged(new PalletRouteChangedRequest(
                "6f1c1d0e-1111-2222-3333-444455556666", new PalletId("p1"), new DepartmentId(20L),
                new DepartmentId(30L)));
    }

    @Test
    void shouldIgnoreEventWhenDeliveryTargetIsShipment() throws Exception {
        final PalletPort palletPort = mock(PalletPort.class);
        final DeliveryRouteChangedKafkaListener listener = new DeliveryRouteChangedKafkaListener(palletPort);

        listener.handle(event("SHIPMENT", "delivery.modified"));

        verify(palletPort, never()).handleDeliveryRouteChanged(any());
    }

    @Test
    void shouldIgnoreEventWhenDeliveryIsNotModified() throws Exception {
        final PalletPort palletPort = mock(PalletPort.class);
        final DeliveryRouteChangedKafkaListener listener = new DeliveryRouteChangedKafkaListener(palletPort);

        listener.handle(event("PALLET", "delivery.completed"));

        verify(palletPort, never()).handleDeliveryRouteChanged(any());
    }

    private DeliveryChangedIntegrationEvent event(final String targetType, final String eventType) throws Exception {
        return JsonMapper.builder().configureForJackson2().build()
                .readValue(MESSAGE.formatted(targetType, eventType), DeliveryChangedIntegrationEvent.class);
    }
}
