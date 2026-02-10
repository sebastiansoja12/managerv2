package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.command.DeviceCreateCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.command.DevicePairRequest;
import com.warehouse.terminal.domain.vo.DeviceTypeChangeCommand;
import com.warehouse.terminal.domain.vo.DeviceUserRequest;
import com.warehouse.terminal.domain.vo.DeviceVersionRequest;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.TerminalIdDto;
import com.warehouse.terminal.request.*;

@Mapper
public interface TerminalRequestMapper {

    default DevicePairRequest map(final DevicePairRequestDto terminalPairRequest) {
        final UserId userId = new UserId(terminalPairRequest.userId().value());
        final DeviceId deviceId = new DeviceId(terminalPairRequest.terminalId().value());
        return new DevicePairRequest(deviceId, userId);
    }

    default TerminalIdDto map(final DeviceId deviceId) {
        return new TerminalIdDto(deviceId != null ? deviceId.value() : null);
    }

    @Mapping(target = "version", source = "version.value")
    @Mapping(target = "departmentCode", ignore = true)
    DeviceCreateCommand map(final DeviceCreateRequestDto terminalAddRequest);

    DeviceTypeChangeCommand map(final DeviceTypeRequestDto deviceTypeRequest);

    default DeviceId map(final DeviceIdDto deviceIdDto) {
        return new DeviceId(deviceIdDto.value());
    }

    DeviceUserRequest map(final DeviceUserRequestDto deviceUserRequest);

    @Mapping(target = "version", source = "version.value")
    DeviceVersionRequest map(final DeviceVersionRequestDto deviceVersionRequest);
}
