package com.warehouse.shipment.infrastructure.adapter.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentDeliveryCommand;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestItemDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseItemDto;

@ExtendWith(MockitoExtension.class)
class ShipmentApiServiceAdapterTest {

    @Mock
    private ShipmentPort shipmentPort;

    private ShipmentApiServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        this.adapter = new ShipmentApiServiceAdapter(this.shipmentPort);
    }

    @Test
    void shouldRejectShipmentAndReturnOriginalIdWhenRelationIsMissing() {
        final Shipment shipment = mock(Shipment.class);
        when(this.shipmentPort.loadShipment(new ShipmentId(1L))).thenReturn(shipment);

        final ShipmentRejectResponseItemDto response = reject(1L, "REJECTED").shipments().getFirst();

        assertEquals(1L, response.shipmentId());
        assertEquals(1L, response.newShipmentId());
        assertTrue(response.loggedInTracker());
        assertTrue(response.success());
        assertNull(response.errorMessage());
    }

    @Test
    void shouldReturnRelatedShipmentIdAfterRejection() {
        final Shipment shipment = mock(Shipment.class);
        when(shipment.getShipmentRelatedId()).thenReturn(new ShipmentId(99L));
        when(this.shipmentPort.loadShipment(new ShipmentId(1L))).thenReturn(shipment);

        final ShipmentRejectResponseItemDto response = reject(1L, "REJECTED").shipments().getFirst();

        assertEquals(99L, response.newShipmentId());
        assertTrue(response.success());
    }

    @Test
    void shouldMapRejectionRequestToDeliveryCommand() {
        final Shipment shipment = mock(Shipment.class);
        when(this.shipmentPort.loadShipment(new ShipmentId(7L))).thenReturn(shipment);
        final ArgumentCaptor<ShipmentDeliveryCommand> commandCaptor =
                ArgumentCaptor.forClass(ShipmentDeliveryCommand.class);

        reject(7L, "REJECTED");

        verify(this.shipmentPort).processShipmentDelivery(commandCaptor.capture());
        assertEquals(new ShipmentId(7L), commandCaptor.getValue().getShipmentId());
        assertEquals(DeliveryMethod.COURIER, commandCaptor.getValue().getDeliveryMethod());
        assertEquals(DeliveryStatus.REJECTED, commandCaptor.getValue().getDeliveryStatus());
    }

    @Test
    void shouldReturnFailureResponseWhenPortRejectsOperation() {
        doThrow(new IllegalStateException("Cannot reject shipment"))
                .when(this.shipmentPort).processShipmentDelivery(
                        org.mockito.ArgumentMatchers.any(ShipmentDeliveryCommand.class));

        final ShipmentRejectResponseItemDto response = reject(5L, "REJECTED").shipments().getFirst();

        assertEquals(5L, response.shipmentId());
        assertEquals(5L, response.newShipmentId());
        assertFalse(response.loggedInTracker());
        assertFalse(response.success());
        assertEquals("Cannot reject shipment", response.errorMessage());
    }

    private ShipmentRejectResponseDto reject(final long shipmentId, final String deliveryStatus) {
        final ShipmentRejectRequestItemDto item = new ShipmentRejectRequestItemDto(
                shipmentId,
                "RETURN",
                deliveryStatus,
                "RETURN"
        );
        return this.adapter.rejectShipment(new ShipmentRejectRequestDto(List.of(item)));
    }
}
