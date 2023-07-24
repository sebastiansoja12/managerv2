package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.ShipmentResponse;
import com.warehouse.shipment.domain.model.UpdateParcelResponse;
import com.warehouse.shipment.infrastructure.api.dto.ParcelDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentResponseDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ShipmentResponseMapper {

    ShipmentResponse map(ShipmentResponseDto responseDto);

    ShipmentResponseDto map(ShipmentResponse response);

    @Mapping(source = "parcel.id", target = "parcel.parcelId.value")
    @Mapping(source = "parcel", target = "parcel")
    UpdateParcelResponseDto map(UpdateParcelResponse updateParcelResponse);

    @Mapping(source = "id", target = "parcelId.value")
    ParcelDto map(Parcel parcel);
}
