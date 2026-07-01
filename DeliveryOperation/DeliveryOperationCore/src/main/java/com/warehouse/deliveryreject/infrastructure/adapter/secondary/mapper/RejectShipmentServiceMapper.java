package com.warehouse.deliveryreject.infrastructure.adapter.secondary.mapper;

import java.util.List;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreject.domain.model.ShipmentRejectRequest;
import com.warehouse.deliveryreject.domain.vo.ShipmentRejectResponse;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectRequestItemDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRejectResponseItemDto;

public class RejectShipmentServiceMapper {

    public ShipmentRejectRequestDto mapRequest(final List<ShipmentRejectRequest> requests) {
        return new ShipmentRejectRequestDto(requests.stream()
                .map(this::mapRequest)
                .toList());
    }

    public List<ShipmentRejectResponse> mapResponse(final ShipmentRejectResponseDto response) {
        return response.shipments()
                .stream()
                .map(this::mapResponse)
                .toList();
    }

    private ShipmentRejectRequestItemDto mapRequest(final ShipmentRejectRequest request) {
        return new ShipmentRejectRequestItemDto(
                request.getShipmentId().getValue(),
                request.getProcessType().name(),
                request.getDeliveryStatus().name(),
                request.getShipmentStatus() == null ? null : request.getShipmentStatus().name()
        );
    }

    private ShipmentRejectResponse mapResponse(final ShipmentRejectResponseItemDto response) {
        return new ShipmentRejectResponse(
                new ShipmentId(response.shipmentId()),
                new ShipmentId(response.newShipmentId()),
                response.loggedInTracker(),
                response.success(),
                response.errorMessage()
        );
    }
}
