package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.request.TerminalAddRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.terminal.domain.model.request.DevicePairRequest;
import com.warehouse.terminal.request.DevicePairRequestDto;

@Mapper
public interface TerminalRequestMapper {

    @Mapping(target = "deviceId.value", source = "terminalId.value")
    DevicePairRequest map(final DevicePairRequestDto terminalPairRequest);

    @Mapping(target = "username", source = "username.value")
    @Mapping(target = "version", source = "version.value")
    @Mapping(target = "departmentCode", source = "departmentCode.value")
    TerminalAddRequest map(final TerminalAddRequestDto terminalAddRequest);

}
