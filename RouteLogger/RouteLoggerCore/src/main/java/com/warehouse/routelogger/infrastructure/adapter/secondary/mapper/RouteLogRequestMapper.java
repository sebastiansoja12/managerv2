package com.warehouse.routelogger.infrastructure.adapter.secondary.mapper;


import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;
import com.warehouse.routelogger.domain.model.Request;
import com.warehouse.routelogger.domain.model.SupplierCodeRequest;
import com.warehouse.routelogger.domain.model.TerminalLogRequest;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.SupplierCodeRequestDto;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.TerminalLogRequestDto;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.TerminalRequestDto;
import org.mapstruct.Mapper;

import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.DeliveryRequestDto;
import org.mapstruct.Mapping;

@Mapper
public interface RouteLogRequestMapper {

    DeliveryRequestDto map(AnyDeliveryRequest anyDeliveryRequest);

    TerminalRequestDto map(Request request);

    SupplierCodeRequestDto map(SupplierCodeRequest request);

    @Mapping(target = "zebraId", source = "terminalId")
    TerminalLogRequestDto map(TerminalLogRequest terminalLogRequest);
}
