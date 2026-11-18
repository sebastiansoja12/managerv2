package com.warehouse.logistics.application.port.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.logistics.application.port.primary.command.ShipmentAcceptCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentAcceptDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentAcceptResult;
import com.warehouse.logistics.application.port.secondary.ProcessHubServicePort;
import com.warehouse.logistics.application.port.secondary.ShipmentAcceptServicePort;
import com.warehouse.logistics.domain.vo.ProcessCommunication;

class ShipmentAcceptPortImplTest {

    private static final ProcessId PROCESS_ID = new ProcessId(UUID.fromString("5a0b7c1d-2e3f-4a5b-8c6d-7e8f9a0b1c2d"));
    private static final TrackingNumber ACCEPTED = new TrackingNumber("TRK-001");
    private static final TrackingNumber FAILED = new TrackingNumber("TRK-002");

    private final ShipmentAcceptServicePort shipmentAcceptServicePort = mock(ShipmentAcceptServicePort.class);
    private final ProcessHubServicePort processHubServicePort = mock(ProcessHubServicePort.class);

    private final ShipmentAcceptPortImpl shipmentAcceptPort = new ShipmentAcceptPortImpl(
            shipmentAcceptServicePort, processHubServicePort);

    @Test
    void shouldAcceptEachShipmentAndReportFailuresPerTrackingNumber() {
        when(shipmentAcceptServicePort.acceptInDepartment(ACCEPTED)).thenReturn(new ShipmentId(100L));
        when(shipmentAcceptServicePort.acceptInDepartment(FAILED))
                .thenThrow(new IllegalStateException("Shipment cannot be accepted in department from status DELIVERY"));

        final ShipmentAcceptResult result = shipmentAcceptPort.accept(
                new ShipmentAcceptCommand(PROCESS_ID, List.of(ACCEPTED, FAILED)));

        assertEquals(List.of(
                ShipmentAcceptDetail.accepted(ACCEPTED, new ShipmentId(100L)),
                ShipmentAcceptDetail.failed(FAILED, "Shipment cannot be accepted in department from status DELIVERY")),
                result.details());
        assertFalse(result.allAccepted());
        verify(processHubServicePort, times(2)).registerCommunication(any(ProcessCommunication.class));
    }
}
