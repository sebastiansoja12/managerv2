package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import com.warehouse.delivery.domain.model.SupplyResponse;
import com.warehouse.supplier.dto.SupplyResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryResponseMapper {
    SupplyResponseDto map(SupplyResponse supplyResponse);
}
