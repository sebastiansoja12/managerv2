package com.warehouse.deliveryhandover;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.warehouse.deliveryhandover.application.port.primary.DeliveryHandoverPortImpl;
import com.warehouse.deliveryhandover.application.port.primary.command.DeliveryHandoverCommand;
import com.warehouse.deliveryhandover.application.port.primary.result.DeliveryHandoverResult;
import com.warehouse.deliveryhandover.application.port.secondary.ShipmentHandoverServicePort;
import com.warehouse.deliveryhandover.application.port.secondary.SupplierConfigurationServicePort;
import com.warehouse.deliveryhandover.domain.exception.EmptyDeliveryHandoverException;
import com.warehouse.deliveryhandover.domain.vo.HandoverResult;

@ExtendWith(MockitoExtension.class)
class DeliveryHandoverPortImplTest {

    private static final ProcessId PROCESS_ID = new ProcessId(UUID.fromString("0b9a1f2e-6c4d-4a7e-8f11-3d2c1b0a9e87"));
    private static final TrackingNumber TRACKING_NUMBER = new TrackingNumber("TRK-001");
    private static final String SIGNATURE = "<svg><path d=\"M0 0 L10 10\"/></svg>";

    @Mock
    private ShipmentHandoverServicePort shipmentHandoverServicePort;

    @Mock
    private SupplierConfigurationServicePort supplierConfigurationServicePort;

    private DeliveryHandoverPortImpl deliveryHandoverPort;

    @BeforeEach
    void setUp() {
        deliveryHandoverPort = new DeliveryHandoverPortImpl(shipmentHandoverServicePort, supplierConfigurationServicePort);
    }

    @Test
    void shouldHandOverShipmentsWithRecipientSignature() {
        when(supplierConfigurationServicePort.isRecipientSignatureRequired()).thenReturn(true);
        when(shipmentHandoverServicePort.handOver(eq(PROCESS_ID), eq(TRACKING_NUMBER), any()))
                .thenReturn(HandoverResult.handedOver(TRACKING_NUMBER, new ShipmentId(100L)));

        final DeliveryHandoverResult result = deliveryHandoverPort.handOver(
                new DeliveryHandoverCommand(PROCESS_ID, List.of(TRACKING_NUMBER), "Jan Kowalski", SIGNATURE));

        assertTrue(result.recipientSigned());
        assertTrue(result.results().getFirst().handedOver());
        assertEquals(new ShipmentId(100L), result.results().getFirst().shipmentId());
    }

    @Test
    void shouldRejectHandoverWithoutSignatureWhenConfigurationRequiresIt() {
        when(supplierConfigurationServicePort.isRecipientSignatureRequired()).thenReturn(true);

        final DeliveryHandoverResult result = deliveryHandoverPort.handOver(
                new DeliveryHandoverCommand(PROCESS_ID, List.of(TRACKING_NUMBER), null, null));

        assertFalse(result.results().getFirst().handedOver());
        assertEquals("Recipient signature is required", result.results().getFirst().errorMessage());
        verify(shipmentHandoverServicePort, never()).handOver(any(), any(), any());
    }

    @Test
    void shouldRequireRecipientNameWhenShipmentIsSigned() {
        when(supplierConfigurationServicePort.isRecipientSignatureRequired()).thenReturn(false);

        final DeliveryHandoverResult result = deliveryHandoverPort.handOver(
                new DeliveryHandoverCommand(PROCESS_ID, List.of(TRACKING_NUMBER), " ", SIGNATURE));

        assertFalse(result.results().getFirst().handedOver());
        assertEquals("Recipient name is required when the recipient signs", result.results().getFirst().errorMessage());
        verify(shipmentHandoverServicePort, never()).handOver(any(), any(), any());
    }

    @Test
    void shouldHandOverWithoutSignatureWhenNotRequiredAndSkipDuplicates() {
        when(supplierConfigurationServicePort.isRecipientSignatureRequired()).thenReturn(false);
        when(shipmentHandoverServicePort.handOver(eq(PROCESS_ID), eq(TRACKING_NUMBER), any()))
                .thenReturn(HandoverResult.handedOver(TRACKING_NUMBER, new ShipmentId(100L)));

        final DeliveryHandoverResult result = deliveryHandoverPort.handOver(
                new DeliveryHandoverCommand(PROCESS_ID, List.of(TRACKING_NUMBER, TRACKING_NUMBER), null, null));

        assertFalse(result.recipientSigned());
        assertTrue(result.results().get(0).handedOver());
        assertFalse(result.results().get(1).handedOver());
        verify(shipmentHandoverServicePort, times(1)).handOver(any(), any(), any());
    }

    @Test
    void shouldRejectEmptyHandover() {
        assertThrows(EmptyDeliveryHandoverException.class, () -> deliveryHandoverPort.handOver(
                new DeliveryHandoverCommand(PROCESS_ID, List.of(), null, null)));
    }
}
