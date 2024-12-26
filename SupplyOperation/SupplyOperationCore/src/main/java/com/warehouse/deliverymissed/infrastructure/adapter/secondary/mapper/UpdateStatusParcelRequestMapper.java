package com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.vo.UpdateStatusShipmentRequest;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto.ShipmentIdDto;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto.ShipmentStatusDto;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto.UpdateStatusShipmentRequestDto;

@Mapper
public interface UpdateStatusParcelRequestMapper {
    default UpdateStatusShipmentRequestDto map(final UpdateStatusShipmentRequest updateStatusShipmentRequest) {
        final ShipmentIdDto shipmentId = map(updateStatusShipmentRequest.getShipmentId());
        final ShipmentStatusDto shipmentStatus = map(updateStatusShipmentRequest.getShipmentStatus());
        return new UpdateStatusShipmentRequestDto(shipmentId, shipmentStatus);
    }

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        final ShipmentIdDto id;
        if (shipmentId == null) {
            id = new ShipmentIdDto(null);
        } else {
            id = new ShipmentIdDto(shipmentId.getValue());
        }
        return id;
    }

    ShipmentStatusDto map(final ShipmentStatus shipmentStatus);
}
