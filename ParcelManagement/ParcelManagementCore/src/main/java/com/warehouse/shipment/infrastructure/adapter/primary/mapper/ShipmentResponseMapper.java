package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentParcelDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentResponseDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateResponseDto;

@Mapper
public interface ShipmentResponseMapper {

    ShipmentResponse map(final ShipmentResponseDto responseDto);

    ShipmentResponseDto map(final ShipmentResponse response);

    ShipmentParcelDto map(final Parcel parcel);

    ShipmentUpdateResponseDto map(final ShipmentUpdateResponse response);
}
