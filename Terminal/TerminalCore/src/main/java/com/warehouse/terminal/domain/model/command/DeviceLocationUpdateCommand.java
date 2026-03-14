package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.vo.DeviceLocation;

public record DeviceLocationUpdateCommand(
        DeviceId deviceId,
        DeviceLocation location
) {
}
