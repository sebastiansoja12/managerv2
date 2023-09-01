package com.warehouse.parcelstate.infrastructure.adapter.primary.mapper;

import com.warehouse.parcelstate.domain.model.DeliveryStateResponse;
import com.warehouse.parcelstate.infrastructure.api.dto.DeliveryStateResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParcelStateResponseMapper {

    DeliveryStateResponseDto map(DeliveryStateResponse response);
}
