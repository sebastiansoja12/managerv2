package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.ShipmentUpdateRequest;
import com.warehouse.shipment.infrastructure.api.dto.ParcelIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShipmentRequestMapper {

    ShipmentRequest map(final ShipmentRequestDto requestDto);

    ShipmentRequestDto map(final ShipmentRequest request);

    ParcelId map(final ParcelIdDto parcelId);

    ShipmentUpdateRequest map(final ShipmentUpdateRequestDto request);
}
