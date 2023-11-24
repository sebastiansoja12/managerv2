package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.delivery.infrastructure.adapter.primary.dto.DeliveryRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.delivery.domain.model.DeliveryRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryRequestMapper {

    @Mapping(target = "deliveryStatus", ignore = true)
    DeliveryRequest map(DeliveryRequestDto deliveryRequest);
    List<DeliveryRequest> map(List<DeliveryRequestDto> deliveryRequest);
}
