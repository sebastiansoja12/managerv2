package com.warehouse.shipment.infrastructure.adapter.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentDeliveryCommand;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.RouteLogRecord;
import com.warehouse.shipment.domain.vo.RouteLogRecordDetail;
import com.warehouse.shipment.domain.vo.RouteLogRecordDetails;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentApiServiceMapper;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestItemDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseItemDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentTrackingDto;
import com.warehouse.shipment.infrastructure.dto.SourceShipmentDto;

@ExtendWith(MockitoExtension.class)
class ShipmentApiServiceAdapterTest {

    @Mock
    private ShipmentPort shipmentPort;

    private ShipmentApiServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        this.adapter = new ShipmentApiServiceAdapter(this.shipmentPort, new ShipmentApiServiceMapper(), new OperatorContext());
    }

    @Test
    void shouldReturnSourceShipmentOfRelatedShipment() {
        when(this.shipmentPort.findSourceShipment(new ShipmentId(2L)))
                .thenReturn(Optional.of(shipmentResult(new ShipmentId(2L))));

        final Optional<SourceShipmentDto> sourceShipment = this.adapter.findSourceShipment(new ShipmentId(2L));

        assertTrue(sourceShipment.isPresent());
        assertEquals(new ShipmentId(1L), sourceShipment.get().shipmentId());
    }

    @Test
    void shouldReturnEmptyWhenShipmentHasNoSourceShipment() {
        when(this.shipmentPort.findSourceShipment(new ShipmentId(2L))).thenReturn(Optional.empty());

        assertTrue(this.adapter.findSourceShipment(new ShipmentId(2L)).isEmpty());
    }

    @Test
    void shouldRejectShipmentAndReturnOriginalIdWhenRelationIsMissing() {
        when(this.shipmentPort.loadShipment(new ShipmentId(1L))).thenReturn(shipmentResult(null));

        final ShipmentRejectResponseItemDto response = reject(1L, "REJECTED").shipments().getFirst();

        assertEquals(1L, response.shipmentId());
        assertEquals(1L, response.newShipmentId());
        assertTrue(response.loggedInTracker());
        assertTrue(response.success());
        assertNull(response.errorMessage());
    }

    @Test
    void shouldReturnRelatedShipmentIdAfterRejection() {
        when(this.shipmentPort.loadShipment(new ShipmentId(1L)))
                .thenReturn(shipmentResult(new ShipmentId(99L)));
        when(this.shipmentPort.loadShipment(new ShipmentId(99L)))
                .thenReturn(new ShipmentResult(new Shipment(new ShipmentId(99L), null, null, null, null, false, null,
                        new TrackingNumber("FTM-REDIRECT-99"), null).snapshot()));

        final ShipmentRejectResponseItemDto response = reject(1L, "REJECTED").shipments().getFirst();

        assertEquals(99L, response.newShipmentId());
        assertEquals("FTM-REDIRECT-99", response.newTrackingNumber());
        assertTrue(response.success());
    }

    @Test
    void shouldKeepRejectionSuccessfulWhenRelatedShipmentCannotBeLoaded() {
        when(this.shipmentPort.loadShipment(new ShipmentId(1L)))
                .thenReturn(shipmentResult(new ShipmentId(98L)));

        final ShipmentRejectResponseItemDto response = reject(1L, "REJECTED").shipments().getFirst();

        assertEquals(98L, response.newShipmentId());
        assertNull(response.newTrackingNumber());
        assertTrue(response.success());
    }

    @Test
    void shouldMapRejectionRequestToDeliveryCommand() {
        when(this.shipmentPort.loadShipment(new ShipmentId(7L))).thenReturn(shipmentResult(null));
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

    private ShipmentResult shipmentResult(final ShipmentId relatedShipmentId) {
        final Shipment shipment = new Shipment(
                new ShipmentId(1L), null, null, relatedShipmentId, null,
                false, null, null, null);
        return new ShipmentResult(shipment.snapshot());
    }

    @Test
    void shouldReturnEmptyRerouteDetailsWhenShipmentDoesNotExist() {
        final TrackingNumber trackingNumber = new TrackingNumber("UNKNOWN");
        when(shipmentPort.findOperator(trackingNumber)).thenReturn(Optional.empty());

        assertTrue(adapter.getShipmentForReroute(trackingNumber).isEmpty());
    }

    @Test
    void shouldReturnTrackingEventsFromNewestToOldest() {
        final TrackingNumber trackingNumber = new TrackingNumber("TRK-001");
        final ProcessId processId = new ProcessId(UUID.fromString("6f0f3a5e-2d0c-4b8e-9a39-2f7d0d3c1a11"));
        final RouteLogRecord routeLog = new RouteLogRecord(processId, new ShipmentId(1L), new RouteLogRecordDetails(Set.of(
                routeEvent(1L, LocalDateTime.of(2026, 10, 20, 8, 0), ShipmentStatus.CREATED),
                routeEvent(2L, LocalDateTime.of(2026, 10, 21, 9, 30), ShipmentStatus.SENT))), null, null);
        when(this.shipmentPort.loadShipmentWithRouteLog(trackingNumber))
                .thenReturn(new ShipmentRouteLog(shipmentResult(null), routeLog, null));

        final ShipmentTrackingDto tracking = this.adapter.getShipmentTracking(trackingNumber);

        assertEquals(new ShipmentId(1L), tracking.shipmentId());
        assertEquals(2, tracking.events().size());
        assertEquals(ShipmentStatus.SENT, tracking.events().getFirst().status());
        assertEquals("KT1", tracking.events().getFirst().departmentCode());
        assertEquals(processId, tracking.events().getFirst().processId());
    }

    @Test
    void shouldReturnNoTrackingEventsWhenRouteLogIsUnavailable() {
        final TrackingNumber trackingNumber = new TrackingNumber("TRK-002");
        when(this.shipmentPort.loadShipmentWithRouteLog(trackingNumber))
                .thenReturn(new ShipmentRouteLog(shipmentResult(null), null, null));

        assertTrue(this.adapter.getShipmentTracking(trackingNumber).events().isEmpty());
    }

    private RouteLogRecordDetail routeEvent(final Long id, final LocalDateTime timestamp, final ShipmentStatus status) {
        return new RouteLogRecordDetail(id, null, "1.0.0", null, "courier", null, null, "KT1", status,
                "Status changed", timestamp, ProcessType.CREATED, "<request/>");
    }
}
