package com.warehouse.logistics.infrastructure.adapter.primary.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logistics.domain.model.CreateDeliveryCommand;
import com.warehouse.logistics.domain.port.primary.LogisticsPort;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ShipmentCreatedIntegrationEventTest {

    @Test
    void shouldReadPayloadFromPublishedEvent() throws Exception {
        final String json = """
                {
                  "payload": {
                    "shipmentId": {"value": 42},
                    "deliveryMethod": "COURIER",
                    "createdAt": "2026-10-07T12:00:00"
                  },
                  "operatorId": {"value": 7},
                  "userId": {"value": 42},
                  "departmentId": {"value": 12},
                  "eventType": "shipment.created"
                }
                """;

        final ShipmentCreatedIntegrationEvent event = new ObjectMapper()
                .readValue(json, ShipmentCreatedIntegrationEvent.class);

        assertEquals(new ShipmentId(42L), event.payload().shipmentId());
        assertEquals(OperatorId.of(7L), event.operatorId());
        assertEquals(new UserId(42L), event.userId());
        assertEquals(new DepartmentId(12L), event.departmentId());
        assertEquals(42L, event.userId().value());
        assertEquals("42", event.eventKey());
    }

    @Test
    void shouldCreateDeliveryFromShipmentCreatedEvent() throws Exception {
        final LogisticsPort logisticsPort = mock(LogisticsPort.class);
        final ShipmentDeliveryEventListener listener = new ShipmentDeliveryEventListener(
                logisticsPort, new OperatorContext());
        final ShipmentCreatedIntegrationEvent event = new ObjectMapper().readValue("""
                {
                  "payload": {"shipmentId": {"value": 42}, "deliveryMethod": "COURIER"},
                  "operatorId": {"value": 7},
                  "userId": {"value": 42},
                  "departmentId": {"value": 12}
                }
                """, ShipmentCreatedIntegrationEvent.class);

        listener.handle(event);

        final ArgumentCaptor<CreateDeliveryCommand> command = ArgumentCaptor.forClass(CreateDeliveryCommand.class);
        verify(logisticsPort).createDelivery(command.capture());
        assertEquals(new ShipmentId(42L), command.getValue().shipmentId());
        assertEquals(42L, command.getValue().userId().value());
    }
}
