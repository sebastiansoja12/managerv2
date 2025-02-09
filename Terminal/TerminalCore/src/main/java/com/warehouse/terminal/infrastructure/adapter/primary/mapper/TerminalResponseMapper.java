package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.vo.DevicePairResponse;
import com.warehouse.terminal.domain.vo.DeviceInformationResponse;
import com.warehouse.terminal.dto.*;
import com.warehouse.terminal.response.DeviceInformationResponseDto;
import com.warehouse.terminal.response.DevicePairResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface TerminalResponseMapper {

    default DeviceDto mapToDeviceResponse(final Terminal terminal) {
        final DepartmentCodeDto depotCode = new DepartmentCodeDto(terminal.getDepartmentCode());
        final DeviceIdDto deviceId = new DeviceIdDto(terminal.getTerminalId().getValue());
        final VersionDto version = new VersionDto(terminal.getVersion());
        final UsernameDto username = new UsernameDto(terminal.getUsername().value());
        return new DeviceDto(deviceId, version, map(terminal.getDeviceType()),
                username, depotCode, terminal.getLastUpdate(), terminal.isActive());
    }

    DeviceTypeDto map(final DeviceType deviceType);

    DeviceInformationResponseDto map(final DeviceInformationResponse deviceInformationResponse);

    DevicePairResponseDto map(final DevicePairResponse devicePairResponse);
}
