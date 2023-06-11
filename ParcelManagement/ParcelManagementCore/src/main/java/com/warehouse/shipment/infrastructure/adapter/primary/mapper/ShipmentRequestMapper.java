package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.domain.model.ShipmentRequest;
import com.warehouse.shipment.domain.model.UpdateParcelRequest;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShipmentRequestMapper {

    ShipmentRequest map(ShipmentRequestDto requestDto);

    ShipmentRequestDto map(ShipmentRequest request);

    @Mapping(source = "parcel.parcelId.value", target = "parcel.id")
    @Mapping(source = "token.value", target = "token")
    UpdateParcelRequest map(UpdateParcelRequestDto updateParcelRequestDto);
}
