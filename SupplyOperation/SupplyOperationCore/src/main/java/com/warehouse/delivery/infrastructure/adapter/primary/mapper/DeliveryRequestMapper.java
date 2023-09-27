package com.warehouse.delivery.infrastructure.adapter.primary.mapper;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.dto.DeliveryRequestDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.SupplyInformationDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryRequestMapper {

    DeliveryRequest map(SupplyInformationDto supplyInformationDto);
    List<DeliveryRequest> map(List<DeliveryRequestDto> deliveryRequest);
}
