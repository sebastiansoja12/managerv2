package com.warehouse.routelogger.infrastructure.adapter.primary.mapper;

import com.warehouse.routelogger.domain.model.DeliveryMissedRequest;
import com.warehouse.routelogger.dto.DeliveryMissedRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {

    DeliveryMissedRequest map(DeliveryMissedRequestDto request);
}
