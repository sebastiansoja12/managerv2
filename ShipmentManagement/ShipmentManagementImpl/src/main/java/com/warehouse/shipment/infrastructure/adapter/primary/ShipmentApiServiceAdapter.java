package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentDeliveryCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentHandoverCommand;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentApiServiceMapper;
import com.warehouse.shipment.infrastructure.dto.*;

import java.util.List;
import java.util.Optional;

public class ShipmentApiServiceAdapter implements ShipmentApiService {

    private final ShipmentPort shipmentPort;
    private final ShipmentApiServiceMapper mapper;
    private final OperatorContext operatorContext;

    public ShipmentApiServiceAdapter(final ShipmentPort shipmentPort,
                                     final ShipmentApiServiceMapper mapper,
                                     final OperatorContext operatorContext) {
        this.shipmentPort = shipmentPort;
        this.mapper = mapper;
        this.operatorContext = operatorContext;
    }

    @Override
    public ShipmentDetailsDto getShipment(final ShipmentId shipmentId) {
        return mapper.map(shipmentPort.loadShipment(shipmentId));
    }

    @Override
    public ShipmentDocumentDto getShipmentDocument(final ShipmentId shipmentId) {
        return mapper.mapDocument(shipmentPort.loadShipment(shipmentId));
    }

    @Override
    public ShipmentTrackingDto getShipmentTracking(final TrackingNumber trackingNumber) {
        return mapper.mapTracking(shipmentPort.loadShipmentWithRouteLog(trackingNumber));
    }

    @Override
    public ShipmentDetailsDto collectShipment(final TrackingNumber trackingNumber) {
        return mapper.map(shipmentPort.collectShipment(trackingNumber));
    }

    @Override
    public ShipmentDetailsDto handOverShipment(final ShipmentHandoverRequestDto shipmentHandoverRequest) {
        return mapper.map(shipmentPort.handOverShipment(new ShipmentHandoverCommand(
                shipmentHandoverRequest.trackingNumber())));
    }

    @Override
    public ShipmentDetailsDto loadShipmentOnPallet(final ShipmentId shipmentId) {
        return mapper.map(shipmentPort.loadShipmentOnPallet(shipmentId));
    }

    @Override
    public Optional<ShipmentRerouteDetailsDto> getShipmentForReroute(final TrackingNumber trackingNumber) {
        return shipmentPort.findOperator(trackingNumber)
                .map(operatorId -> operatorContext.runAs(operatorId,
                        () -> mapper.map(shipmentPort.loadShipmentForReroute(trackingNumber))));
    }

    @Override
    public Optional<SourceShipmentDto> findSourceShipment(final ShipmentId shipmentId) {
        return shipmentPort.findSourceShipment(shipmentId)
                .map(sourceShipment -> new SourceShipmentDto(sourceShipment.snapshot().shipmentId(),
                        sourceShipment.snapshot().shipmentStatus()));
    }

    @Override
    public ShipmentRejectResponseDto rejectShipment(final ShipmentRejectRequestDto shipmentRejectRequest) {
        final List<ShipmentRejectResponseItemDto> responses = shipmentRejectRequest.shipments()
                .stream()
                .map(this::rejectShipment)
                .toList();

        return new ShipmentRejectResponseDto(responses);
    }

    private ShipmentRejectResponseItemDto rejectShipment(final ShipmentRejectRequestItemDto shipmentRejectRequest) {
        final ShipmentId shipmentId = new ShipmentId(shipmentRejectRequest.shipmentId());
        final ShipmentDeliveryCommand command = new ShipmentDeliveryCommand(shipmentId, DeliveryMethod.COURIER, null,
                DeliveryStatus.valueOf(shipmentRejectRequest.deliveryStatus()));

        try {
            this.shipmentPort.processShipmentDelivery(command);

            final ShipmentResult shipment = this.shipmentPort.loadShipment(shipmentId);
            final ShipmentId relatedShipmentId = shipment.snapshot().shipmentRelatedId();
            final ShipmentId newShipmentId = relatedShipmentId == null ? shipmentId : relatedShipmentId;

            return new ShipmentRejectResponseItemDto(shipmentId.getValue(), newShipmentId.getValue(), true, true, null,
                    trackingNumber(relatedShipmentId == null ? shipment : loadRelatedShipment(relatedShipmentId)));
        } catch (final RuntimeException e) {
            return new ShipmentRejectResponseItemDto(shipmentId.getValue(), shipmentId.getValue(), false, false,
                    e.getMessage(), null);
        }
    }
    // The rejection already happened; a missing related shipment must not turn it into a failure.
    private ShipmentResult loadRelatedShipment(final ShipmentId relatedShipmentId) {
        try {
            return this.shipmentPort.loadShipment(relatedShipmentId);
        } catch (final RuntimeException exception) {
            return null;
        }
    }

    private static String trackingNumber(final ShipmentResult shipment) {
        return shipment == null || shipment.snapshot().trackingNumber() == null
                ? null
                : shipment.snapshot().trackingNumber().value();
    }

    @Override
    public void markReturned(final ShipmentId shipmentId) {
        shipmentPort.markReturned(shipmentId);
    }

    @Override
    public ShipmentReturnResponseDto returnShipment(final ShipmentId shipmentId) {
        shipmentPort.markReturned(shipmentId);
        final ShipmentId returnShipmentId = shipmentPort.loadShipment(shipmentId).snapshot().shipmentRelatedId();
        return new ShipmentReturnResponseDto(shipmentId, returnShipmentId,
                returnShipmentId == null ? null : trackingNumber(loadRelatedShipment(returnShipmentId)));
    }

    @Override
    public void markDelivered(final ShipmentId shipmentId) {
        shipmentPort.markDelivered(shipmentId);
    }

    @Override
    public void restoreAfterReturnCancellation(final ShipmentId shipmentId) {
        shipmentPort.restoreAfterReturnCancellation(shipmentId);
    }

    @Override
    public void completeReturn(final ShipmentId shipmentId) {
        shipmentPort.notifyShipmentReturnCompleted(shipmentId);
    }

    @Override
    public void cancelReturnShipment(final ShipmentId shipmentId) {
        shipmentPort.notifyShipmentReturnCanceled(shipmentId);
    }

    @Override
    public boolean hasAnyShipment() {
        return shipmentPort.hasAnyShipment();
    }
}
