package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.vo.DeviceTypeRequest;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import com.warehouse.terminal.request.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.terminal.domain.model.request.DevicePairRequest;

@Mapper
public interface TerminalRequestMapper {

    @Mapping(target = "deviceId.value", source = "terminalId.value")
    DevicePairRequest map(final DevicePairRequestDto terminalPairRequest);

    @Mapping(target = "username", source = "username.value")
    @Mapping(target = "version", source = "version.value")
    @Mapping(target = "departmentCode", source = "departmentCode.value")
    TerminalAddRequest map(final TerminalAddRequestDto terminalAddRequest);

    DeviceTypeRequest map(final DeviceTypeRequestDto deviceTypeRequest);

    DeviceUserRequest map(final DeviceUserRequestDto deviceUserRequest);

    @Mapping(target = "version", source = "version.value")
    DeviceVersionRequest map(final DeviceVersionRequestDto deviceVersionRequest);
}
