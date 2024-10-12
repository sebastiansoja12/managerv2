package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import org.mapstruct.Mapper;

import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentResponseDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateResponseDto;

@Mapper
public interface ShipmentResponseMapper {

    ShipmentResponse map(final ShipmentResponseDto responseDto);

    ShipmentResponseDto map(final ShipmentResponse response);

    ShipmentDto map(final Shipment shipment);

    ShipmentUpdateResponseDto map(final ShipmentUpdateResponse response);

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        return new ShipmentIdDto(shipmentId.getValue());
    }
}
