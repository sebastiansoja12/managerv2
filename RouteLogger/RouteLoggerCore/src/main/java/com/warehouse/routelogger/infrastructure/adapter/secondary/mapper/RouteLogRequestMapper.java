package com.warehouse.routelogger.infrastructure.adapter.secondary.mapper;


import com.warehouse.routelogger.domain.model.*;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.*;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper
public interface RouteLogRequestMapper {

    DeliveryRequestDto map(AnyDeliveryRequest anyDeliveryRequest);

    TerminalRequestDto map(Request request);

    SupplierCodeRequestDto map(SupplierCodeRequest request);

    @Mapping(target = "zebraId", source = "terminalId")
    TerminalLogRequestDto map(TerminalLogRequest terminalLogRequest);

    VersionLogRequestDto map(VersionLogRequest versionLogRequest);

    UsernameLogRequestDto map(UsernameLogRequest request);

    DeliveryRequestDto mapToDepotCodeRequest(DepotCodeRequest depotCodeRequest);
}
