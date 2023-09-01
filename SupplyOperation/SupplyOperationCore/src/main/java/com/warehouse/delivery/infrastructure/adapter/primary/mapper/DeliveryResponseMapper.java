package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.supplier.dto.SupplyResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryResponseMapper {
    SupplyResponseDto map(DeliveryResponse deliveryResponse);

    List<SupplyResponseDto> map(List<DeliveryResponse> responses);
}
