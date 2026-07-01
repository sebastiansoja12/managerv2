package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;

public record DeviceTypeUpdateCommand(
        DeviceId deviceId,
        DeviceType deviceType
) {
}
