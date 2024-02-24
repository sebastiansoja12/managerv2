package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryMapper {

    DeliveryResponse mapToSupplyResponse(DeliveryRequest deliveryRequest);

    DeliveryRouteResponse map(DeliveryRouteRequest deliveryRouteRequest);
}
