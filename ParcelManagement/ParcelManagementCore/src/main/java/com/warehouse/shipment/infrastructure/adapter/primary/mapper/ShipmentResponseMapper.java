package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentDto;
import org.mapstruct.Mapper;

import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentResponseDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateResponseDto;

@Mapper
public interface ShipmentResponseMapper {

    ShipmentResponse map(final ShipmentResponseDto responseDto);

    ShipmentResponseDto map(final ShipmentResponse response);

    ShipmentDto map(final Parcel parcel);

    ShipmentUpdateResponseDto map(final ShipmentUpdateResponse response);
}
