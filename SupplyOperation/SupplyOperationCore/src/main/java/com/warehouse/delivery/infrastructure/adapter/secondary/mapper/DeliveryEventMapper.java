package com.warehouse.delivery.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.delivery.domain.vo.DepartmentCodeRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.routelogger.dto.*;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper
public interface DeliveryEventMapper {

    @Mapping(target = "processType", constant = "MISS")
    DeliveryRequestDto map(DeliveryMissed deliveryMissed);

    DepotCodeRequestDto mapToDepotCodeRequest(final DepartmentCodeRequest departmentCodeRequest);

    @Mapping(target = "request", source = "requestAsJson")
    RequestDto map(TerminalRequest terminalRequest, String requestAsJson);

    @Mapping(target = "processType", constant = "MISS")
    SupplierCodeRequestDto mapToSupplierCodeRequest(DeliveryMissed deliveryMissed);

    @Mapping(target = "terminalId", source = "device.deviceId")
    TerminalLogRequestDto mapToTerminalLogRequest(TerminalRequest terminalRequest);

    @Mapping(target = "version", source = "device.version")
    VersionLogRequestDto mapToVersionLogRequest(TerminalRequest terminalRequest);
}
