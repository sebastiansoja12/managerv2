package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDerivedDeliveryCommand;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ShipmentDeliveryEventListenerTest {

    @Test
    void shouldReadPayloadFromPublishedEvent() throws Exception {
        final String json = """
                {
                  "payload": {
                    "shipmentId": {"value": 42},
                    "createdAt": "2026-10-07T12:00:00"
                  },
                  "operatorId": {"value": 7},
                  "userId": {"value": 42},
                  "departmentId": {"value": 12},
                  "eventType": "shipment.created"
                }
                """;

        final ShipmentCreatedIntegrationEvent event = new ObjectMapper().findAndRegisterModules()
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
        final DeliveryPort deliveryPort = mock(DeliveryPort.class);
        final ShipmentDeliveryEventListener listener = new ShipmentDeliveryEventListener(
                deliveryPort, new OperatorContext());
        final ShipmentCreatedIntegrationEvent event = new ObjectMapper().readValue("""
                {
                  "payload": {"shipmentId": {"value": 42}},
                  "delivery": {
                    "originDepartmentId": {"value": 3},
                    "destinationDepartmentId": {"value": 5},
                    "pickupMethod": "PICKUP_POINT",
                    "deliveryMethod": "LOCKER",
                    "pickupPointId": {"value": "7d4bd2b0-5b55-4c0f-9a43-8a8f8a7e1d10"},
                    "deliveryPickupPointId": {"value": "0c7a5d0e-4b0f-4c64-9f53-1b2f62c6a8b1"}
                  },
                  "operatorId": {"value": 7},
                  "userId": {"value": 42},
                  "departmentId": {"value": 12}
                }
                """, ShipmentCreatedIntegrationEvent.class);

        listener.handle(event);

        final ArgumentCaptor<CreateDeliveryCommand> command = ArgumentCaptor.forClass(CreateDeliveryCommand.class);
        verify(deliveryPort).createDelivery(command.capture());
        assertEquals(new ShipmentId(42L), command.getValue().shipmentId());
        assertEquals(42L, command.getValue().userId().value());
        assertEquals(new DepartmentId(3L), command.getValue().originDepartmentId());
        assertEquals(new DepartmentId(5L), command.getValue().destinationDepartmentId());
        assertEquals(PickupMethod.PICKUP_POINT, command.getValue().pickupMethod());
        assertEquals(DeliveryMethod.LOCKER, command.getValue().method());
        assertEquals(new PickupPointId(UUID.fromString("7d4bd2b0-5b55-4c0f-9a43-8a8f8a7e1d10")),
                command.getValue().pickupPointId());
        assertEquals(new PickupPointId(UUID.fromString("0c7a5d0e-4b0f-4c64-9f53-1b2f62c6a8b1")),
                command.getValue().deliveryPickupPointId());
    }

    @Test
    void shouldCreateDerivedDeliveryWhenShipmentCreatedEventHasNoDeliveryData() throws Exception {
        final DeliveryPort deliveryPort = mock(DeliveryPort.class);
        final ShipmentDeliveryEventListener listener = new ShipmentDeliveryEventListener(
                deliveryPort, new OperatorContext());
        final ShipmentCreatedIntegrationEvent event = new ObjectMapper().readValue("""
                {
                  "payload": {"shipmentId": {"value": 43}, "signatureRequired": true},
                  "operatorId": {"value": 7},
                  "userId": {"value": 42},
                  "departmentId": {"value": 12}
                }
                """, ShipmentCreatedIntegrationEvent.class);

        listener.handle(event);

        final ArgumentCaptor<CreateDerivedDeliveryCommand> command =
                ArgumentCaptor.forClass(CreateDerivedDeliveryCommand.class);
        verify(deliveryPort).createDerivedDelivery(command.capture());
        verify(deliveryPort, never()).createDelivery(any());
        assertEquals(new ShipmentId(43L), command.getValue().shipmentId());
        assertEquals(Boolean.TRUE, command.getValue().signatureRequired());
    }

    @Test
    void shouldChangeDestinationDepartmentFromDestinationChangedEvent() throws Exception {
        final DeliveryPort deliveryPort = mock(DeliveryPort.class);
        final ShipmentDeliveryEventListener listener = new ShipmentDeliveryEventListener(
                deliveryPort, new OperatorContext());
        final ShipmentDestinationChangedIntegrationEvent event = new ObjectMapper().readValue("""
                {
                  "payload": {"shipmentId": {"value": 42}},
                  "destinationDepartmentId": {"value": 9},
                  "operatorId": {"value": 7},
                  "userId": {"value": 42},
                  "departmentId": {"value": 12}
                }
                """, ShipmentDestinationChangedIntegrationEvent.class);

        listener.handle(event);

        verify(deliveryPort).changeDestinationDepartment(new ShipmentId(42L), new DepartmentId(9L));
        verify(deliveryPort, never()).completeDelivery(any());
    }
}
