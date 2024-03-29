package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.UpdateParcelRequest;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShipmentRequestMapper {

    ShipmentRequest map(ShipmentRequestDto requestDto);

    ShipmentRequestDto map(ShipmentRequest request);

    @Mapping(source = "parcel.parcelId.value", target = "parcel.id")
    UpdateParcelRequest map(UpdateParcelRequestDto updateParcelRequestDto);
}
