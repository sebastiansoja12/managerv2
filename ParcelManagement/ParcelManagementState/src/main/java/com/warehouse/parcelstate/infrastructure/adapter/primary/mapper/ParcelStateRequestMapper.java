package com.warehouse.parcelstate.infrastructure.adapter.primary.mapper;

import com.warehouse.parcelstate.domain.model.DeliveryStateRequest;
import com.warehouse.parcelstate.infrastructure.api.dto.DeliveryStateRequestDto;
import org.mapstruct.Mapper;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParcelStateRequestMapper {

    DeliveryStateRequest map(DeliveryStateRequestDto request);
}
