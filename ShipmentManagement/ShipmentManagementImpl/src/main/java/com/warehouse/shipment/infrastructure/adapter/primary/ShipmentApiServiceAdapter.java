package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentDeliveryCommand;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentApiServiceMapper;
import com.warehouse.shipment.infrastructure.dto.*;

import java.util.List;

public class ShipmentApiServiceAdapter implements ShipmentApiService {

    private final ShipmentPort shipmentPort;
    private final ShipmentApiServiceMapper mapper;

    public ShipmentApiServiceAdapter(final ShipmentPort shipmentPort, final ShipmentApiServiceMapper mapper) {
        this.shipmentPort = shipmentPort;
        this.mapper = mapper;
    }

    @Override
    public ShipmentDetailsDto getShipment(final ShipmentId shipmentId) {
        return mapper.map(shipmentPort.loadShipment(shipmentId));
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

            return new ShipmentRejectResponseItemDto(shipmentId.getValue(), newShipmentId.getValue(), true, true, null);
        } catch (final RuntimeException e) {
            return new ShipmentRejectResponseItemDto(shipmentId.getValue(), shipmentId.getValue(), false, false,
                    e.getMessage());
        }
    }
    @Override
    public void markReturned(final ShipmentId shipmentId) {
        shipmentPort.markReturned(shipmentId);
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
}
