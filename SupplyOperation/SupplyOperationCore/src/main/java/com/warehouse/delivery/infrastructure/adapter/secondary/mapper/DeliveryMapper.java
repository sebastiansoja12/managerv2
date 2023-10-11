package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;
import com.warehouse.routetracker.infrastructure.api.dto.DeliveryInformationDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryMapper {

    DeliveryResponse mapToSupplyResponse(DeliveryRequest deliveryRequest);

    DeliveryRouteResponse map(DeliveryRouteRequest deliveryRouteRequest);

    List<DeliveryInformationDto> map(Set<DeliveryRouteRequest> deliveryRequest);
}
