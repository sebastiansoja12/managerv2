package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.route.infrastructure.api.dto.SupplyInformationDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryMapper {

    SupplyInformationDto map(DeliveryRequest deliveryRequest);

    List<SupplyInformationDto> map(List<DeliveryRequest> deliveryRequest);

    DeliveryResponse mapToSupplyResponse(DeliveryRequest deliveryRequest);
}
