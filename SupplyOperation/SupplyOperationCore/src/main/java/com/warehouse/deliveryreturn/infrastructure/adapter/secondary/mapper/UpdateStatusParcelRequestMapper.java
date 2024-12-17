package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusShipmentRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentIdDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentStatusDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.UpdateStatusShipmentRequestDto;

@Mapper
public interface UpdateStatusParcelRequestMapper {
    default UpdateStatusShipmentRequestDto map(final UpdateStatusShipmentRequest request) {
        final ShipmentIdDto shipmentId = map(request.getShipmentId());
        final ShipmentStatusDto shipmentStatus = map(request.getShipmentStatus());
        return new UpdateStatusShipmentRequestDto(shipmentId, shipmentStatus);
    }

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        return new ShipmentIdDto(shipmentId.getValue());
    }

    ShipmentStatusDto map(final ShipmentStatus shipmentStatus);
}
