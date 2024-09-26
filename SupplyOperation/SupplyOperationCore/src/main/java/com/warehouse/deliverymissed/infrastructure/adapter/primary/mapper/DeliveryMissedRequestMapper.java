package com.warehouse.deliverymissed.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.deliverymissed.domain.vo.DeliveryMissedRequest;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper
public interface DeliveryMissedRequestMapper {


    @Mapping(target = "depotCode", source = "terminalDeviceInformation.depotCode")
    @Mapping(target = "supplierCode", source = "terminalDeviceInformation.username")
    DeliveryMissedRequest map(TerminalRequest terminalRequest);

}
