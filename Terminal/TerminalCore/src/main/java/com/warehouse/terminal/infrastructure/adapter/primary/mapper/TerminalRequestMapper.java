package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.request.DevicePairRequest;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.domain.vo.DeviceTypeRequest;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.TerminalIdDto;
import com.warehouse.terminal.request.*;

@Mapper
public interface TerminalRequestMapper {

    DevicePairRequest map(final DevicePairRequestDto terminalPairRequest);

    default TerminalIdDto map(final DeviceId deviceId) {
        return new TerminalIdDto(deviceId != null ? deviceId.getValue() : null);
    }

    @Mapping(target = "version", source = "version.value")
    @Mapping(target = "departmentCode", source = "departmentCode.value")
    TerminalAddRequest map(final TerminalAddRequestDto terminalAddRequest);

    DeviceTypeRequest map(final DeviceTypeRequestDto deviceTypeRequest);

    default DeviceId map(final DeviceIdDto deviceIdDto) {
        return new DeviceId(deviceIdDto.value());
    }

    DeviceUserRequest map(final DeviceUserRequestDto deviceUserRequest);

    @Mapping(target = "version", source = "version.value")
    DeviceVersionRequest map(final DeviceVersionRequestDto deviceVersionRequest);
}
