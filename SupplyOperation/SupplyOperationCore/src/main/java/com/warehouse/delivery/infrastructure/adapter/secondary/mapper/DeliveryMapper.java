package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;
import com.warehouse.route.infrastructure.api.dto.SupplyInformationDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryMapper {

    SupplyInformationDto map(DeliveryRequest deliveryRequest);

    List<SupplyInformationDto> map(List<DeliveryRequest> deliveryRequest);

    DeliveryResponse mapToSupplyResponse(DeliveryRequest deliveryRequest);

    DeliveryRouteResponse map(DeliveryRouteRequest deliveryRouteRequest);

    List<SupplyInformationDto> map(Set<DeliveryRouteRequest> deliveryRequest);
}
