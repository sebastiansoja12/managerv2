package com.warehouse.terminal.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.dto.*;
import org.mapstruct.Mapper;

@Mapper
public interface TerminalResponseMapper {

    default DeviceDto mapToDeviceResponse(final Terminal terminal) {
        final DepotCodeDto depotCode = new DepotCodeDto(terminal.getDepotCode());
        final DeviceIdDto deviceId = new DeviceIdDto(terminal.getTerminalId().getValue());
        final VersionDto version = new VersionDto(terminal.getVersion());
        final UserIdDto userId = new UserIdDto(terminal.getUserId().getValue());
        return new DeviceDto(deviceId, version, map(terminal.getDeviceType()),
                userId, depotCode, terminal.getLastUpdate(), terminal.isActive());
    }

    DeviceTypeDto map(final DeviceType deviceType);
}
