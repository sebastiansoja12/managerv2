package com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper;

import com.warehouse.routelogger.dto.DeliveryRequestDto;
import com.warehouse.routelogger.dto.DepotCodeRequestDto;
import com.warehouse.routelogger.dto.RequestDto;
import com.warehouse.routelogger.dto.SupplierCodeRequestDto;
import com.warehouse.terminal.request.TerminalRequest;
import org.mapstruct.Mapper;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import org.mapstruct.Mapping;

@Mapper
public interface DeliveryMissedEventMapper {

    @Mapping(target = "processType", constant = "MISS")
    DeliveryRequestDto map(DeliveryMissed deliveryMissed);

    @Mapping(target = "processType", constant = "MISS")
    DepotCodeRequestDto mapToDepotCodeRequest(DeliveryMissed deliveryMissed);

    @Mapping(target = "request", source = "requestAsJson")
    RequestDto map(TerminalRequest terminalRequest, String requestAsJson);

    @Mapping(target = "processType", constant = "MISS")
    SupplierCodeRequestDto mapToSupplierCodeRequest(DeliveryMissed deliveryMissed);
}
