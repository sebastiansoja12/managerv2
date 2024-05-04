package com.warehouse.routelogger.infrastructure.adapter.secondary.mapper;


import org.mapstruct.Mapper;

import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;
import com.warehouse.routelogger.domain.model.Request;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.DeliveryRequestDto;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.TerminalRequestDto;

@Mapper
public interface RouteLogRequestMapper {

    DeliveryRequestDto map(AnyDeliveryRequest anyDeliveryRequest);

    TerminalRequestDto map(Request request);
}
