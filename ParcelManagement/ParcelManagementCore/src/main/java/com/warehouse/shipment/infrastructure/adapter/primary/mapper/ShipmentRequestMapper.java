package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.ShipmentUpdateRequest;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentUpdateRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShipmentRequestMapper {

    ShipmentRequest map(final ShipmentRequestDto requestDto);

    ShipmentRequestDto map(final ShipmentRequest request);

    ShipmentId map(final ShipmentIdDto parcelId);

    ShipmentUpdateRequest map(final ShipmentUpdateRequestDto request);
}
