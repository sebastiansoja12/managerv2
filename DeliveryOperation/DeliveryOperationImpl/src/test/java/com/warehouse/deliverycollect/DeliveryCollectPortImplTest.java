package com.warehouse.deliverycollect;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverycollect.application.port.primary.DeliveryCollectPortImpl;
import com.warehouse.deliverycollect.application.port.primary.command.DeliveryCollectCommand;
import com.warehouse.deliverycollect.application.port.primary.result.DeliveryCollectResult;
import com.warehouse.deliverycollect.application.port.secondary.ShipmentCollectServicePort;
import com.warehouse.deliverycollect.domain.exception.EmptyDeliveryCollectException;
import com.warehouse.deliverycollect.domain.vo.CollectResult;

@ExtendWith(MockitoExtension.class)
class DeliveryCollectPortImplTest {

    private static final ProcessId PROCESS_ID = new ProcessId(UUID.fromString("6f0f3a5e-2d0c-4b8e-9a39-2f7d0d3c1a11"));

    @Mock
    private ShipmentCollectServicePort shipmentCollectServicePort;

    private DeliveryCollectPortImpl deliveryCollectPort;

    @BeforeEach
    void setUp() {
        deliveryCollectPort = new DeliveryCollectPortImpl(shipmentCollectServicePort);
    }

    @Test
    void shouldReturnResultForEveryTrackingNumberWhenOneShipmentCannotBeCollected() {
        final TrackingNumber collectedTrackingNumber = new TrackingNumber("TRK-001");
        final TrackingNumber rejectedTrackingNumber = new TrackingNumber("TRK-002");
        when(shipmentCollectServicePort.collect(PROCESS_ID, collectedTrackingNumber))
                .thenReturn(CollectResult.collected(collectedTrackingNumber, new ShipmentId(100L)));
        when(shipmentCollectServicePort.collect(PROCESS_ID, rejectedTrackingNumber))
                .thenReturn(CollectResult.failed(rejectedTrackingNumber, "Shipment was not found"));

        final DeliveryCollectResult result = deliveryCollectPort.collect(new DeliveryCollectCommand(PROCESS_ID,
                List.of(collectedTrackingNumber, rejectedTrackingNumber)));

        assertAll(
                () -> assertEquals(2, result.results().size()),
                () -> assertTrue(result.results().get(0).collected()),
                () -> assertEquals(new ShipmentId(100L), result.results().get(0).shipmentId()),
                () -> assertFalse(result.results().get(1).collected()),
                () -> assertEquals("Shipment was not found", result.results().get(1).errorMessage())
        );
    }

    @Test
    void shouldCollectDuplicatedTrackingNumberOnlyOnce() {
        final TrackingNumber trackingNumber = new TrackingNumber("TRK-001");
        when(shipmentCollectServicePort.collect(PROCESS_ID, trackingNumber))
                .thenReturn(CollectResult.collected(trackingNumber, new ShipmentId(100L)));

        final DeliveryCollectResult result = deliveryCollectPort.collect(new DeliveryCollectCommand(PROCESS_ID,
                List.of(trackingNumber, trackingNumber)));

        assertAll(
                () -> assertTrue(result.results().get(0).collected()),
                () -> assertFalse(result.results().get(1).collected()),
                () -> assertEquals("Tracking number is duplicated in request", result.results().get(1).errorMessage())
        );
        verify(shipmentCollectServicePort, times(1)).collect(PROCESS_ID, trackingNumber);
    }

    @Test
    void shouldRejectBlankTrackingNumberWithoutCallingShipmentService() {
        final TrackingNumber blankTrackingNumber = new TrackingNumber(" ");

        final DeliveryCollectResult result = deliveryCollectPort.collect(new DeliveryCollectCommand(PROCESS_ID,
                List.of(blankTrackingNumber)));

        assertEquals("Tracking number is required", result.results().get(0).errorMessage());
        verify(shipmentCollectServicePort, never()).collect(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenRequestDoesNotContainTrackingNumbers() {
        final DeliveryCollectCommand command = new DeliveryCollectCommand(PROCESS_ID, List.of());

        assertThrows(EmptyDeliveryCollectException.class, () -> deliveryCollectPort.collect(command));
    }
}
