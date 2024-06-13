package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.routelogger.dto.*;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper
public interface DeliveryReturnEventMapper {

    @Mapping(target = "processType", constant = "MISS")
    DeliveryRequestDto map(DeliveryMissed deliveryMissed);

    @Mapping(target = "processType", constant = "RETURN")
    @Mapping(target = "deviceInformation.depotCode", constant = "depotCode")
    DepotCodeRequestDto mapToDepotCodeRequest(DeliveryReturnRequest deliveryReturnRequest);

    @Mapping(target = "request", source = "requestAsJson")
    RequestDto map(TerminalRequest terminalRequest, String requestAsJson);

    @Mapping(target = "processType", constant = "RETURN")
    SupplierCodeRequestDto mapToSupplierCodeRequest(DeliveryReturn deliveryReturn);

    @Mapping(target = "terminalId", source = "terminalDeviceInformation.terminalId")
    TerminalLogRequestDto mapToTerminalLogRequest(TerminalRequest terminalRequest);

    @Mapping(target = "version", source = "terminalDeviceInformation.version")
    VersionLogRequestDto mapToVersionLogRequest(TerminalRequest terminalRequest);
}
