package com.warehouse.logistics.application.port.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.logistics.application.port.primary.command.ShipmentHandoverCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverResult;
import com.warehouse.logistics.application.port.secondary.DeliveryHandoverServicePort;
import com.warehouse.logistics.application.port.secondary.DeliveryServicePort;
import com.warehouse.logistics.application.port.secondary.ProcessHubServicePort;
import com.warehouse.logistics.domain.vo.ProcessCommunication;

class ShipmentHandoverPortImplTest {

    private static final ProcessId PROCESS_ID = new ProcessId(UUID.fromString("5a0b7c1d-2e3f-4a5b-8c6d-7e8f9a0b1c2d"));
    private static final TrackingNumber DELIVERED = new TrackingNumber("TRK-001");
    private static final TrackingNumber REJECTED = new TrackingNumber("TRK-002");

    private final DeliveryHandoverServicePort deliveryHandoverServicePort = mock(DeliveryHandoverServicePort.class);
    private final DeliveryServicePort deliveryServicePort = mock(DeliveryServicePort.class);
    private final ProcessHubServicePort processHubServicePort = mock(ProcessHubServicePort.class);

    private final ShipmentHandoverPortImpl shipmentHandoverPort = new ShipmentHandoverPortImpl(
            deliveryHandoverServicePort, deliveryServicePort, processHubServicePort);

    @Test
    void shouldCompleteDeliveryOnlyForHandedOverShipments() {
        final ShipmentHandoverCommand command = new ShipmentHandoverCommand(PROCESS_ID, List.of(DELIVERED, REJECTED),
                "Jan Kowalski", "<svg/>");
        when(deliveryHandoverServicePort.handOver(command)).thenReturn(new ShipmentHandoverResult(List.of(
                new ShipmentHandoverDetail(DELIVERED, new ShipmentId(100L), true, null),
                new ShipmentHandoverDetail(REJECTED, null, false, "Shipment cannot be handed over from status DELIVERY")),
                true));

        final ShipmentHandoverResult result = shipmentHandoverPort.handOver(command);

        verify(deliveryServicePort).completeShipmentDelivery(new ShipmentId(100L), true, "Jan Kowalski", "<svg/>");
        verify(deliveryServicePort, never()).completeShipmentDelivery(eq(null), anyBoolean(), any(), any());
        final ArgumentCaptor<ProcessCommunication> communication = ArgumentCaptor.forClass(ProcessCommunication.class);
        verify(processHubServicePort).registerCommunication(communication.capture());
        assertEquals(PROCESS_ID, communication.getValue().processId());
        assertEquals(ProcessType.HANDOVER, communication.getValue().processType());
        assertNull(communication.getValue().faultDescription());
        assertTrue(communication.getValue().response().contains("completed=true"));
        assertTrue(result.details().get(0).handedOver());
        assertFalse(result.details().get(1).handedOver());
        assertFalse(result.allHandedOver());
    }

    @Test
    void shouldReportShipmentAsNotHandedOverWhenDeliveryCannotBeCompleted() {
        final ShipmentHandoverCommand command = new ShipmentHandoverCommand(PROCESS_ID, List.of(DELIVERED), null, null);
        when(deliveryHandoverServicePort.handOver(command)).thenReturn(new ShipmentHandoverResult(List.of(
                new ShipmentHandoverDetail(DELIVERED, new ShipmentId(100L), true, null)), false));
        doThrow(new IllegalStateException("Recipient signature is required for this delivery"))
                .when(deliveryServicePort).completeShipmentDelivery(new ShipmentId(100L), false, null, null);

        final ShipmentHandoverResult result = shipmentHandoverPort.handOver(command);

        assertFalse(result.details().getFirst().handedOver());
        assertEquals("Recipient signature is required for this delivery", result.details().getFirst().errorMessage());
        assertEquals(new ShipmentId(100L), result.details().getFirst().shipmentId());
        final ArgumentCaptor<ProcessCommunication> communication = ArgumentCaptor.forClass(ProcessCommunication.class);
        verify(processHubServicePort).registerCommunication(communication.capture());
        assertEquals("Recipient signature is required for this delivery", communication.getValue().faultDescription());
        assertNull(communication.getValue().response());
    }
}
