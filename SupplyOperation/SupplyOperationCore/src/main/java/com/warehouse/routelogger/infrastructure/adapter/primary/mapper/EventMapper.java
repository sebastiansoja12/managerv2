package com.warehouse.routelogger.infrastructure.adapter.primary.mapper;

import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;
import com.warehouse.routelogger.dto.DeliveryRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {

    AnyDeliveryRequest map(DeliveryRequestDto request);
}
