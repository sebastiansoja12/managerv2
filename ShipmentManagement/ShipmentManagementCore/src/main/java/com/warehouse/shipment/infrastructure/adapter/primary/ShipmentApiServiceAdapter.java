package com.warehouse.shipment.infrastructure.adapter.primary;

import java.util.List;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentDeliveryCommand;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestItemDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseItemDto;

public class ShipmentApiServiceAdapter implements ShipmentApiService {

    private final ShipmentPort shipmentPort;

    public ShipmentApiServiceAdapter(final ShipmentPort shipmentPort) {
        this.shipmentPort = shipmentPort;
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

            final Shipment shipment = this.shipmentPort.loadShipment(shipmentId);
            final ShipmentId newShipmentId = shipment.getShipmentRelatedId() == null ? shipmentId : shipment.getShipmentRelatedId();

            return new ShipmentRejectResponseItemDto(shipmentId.getValue(), newShipmentId.getValue(), true, true, null);
        } catch (final RuntimeException e) {
            return new ShipmentRejectResponseItemDto(shipmentId.getValue(), shipmentId.getValue(), false, false,
                    e.getMessage());
        }
    }
}
