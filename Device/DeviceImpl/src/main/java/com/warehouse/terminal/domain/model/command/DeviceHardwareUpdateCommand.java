package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.vo.DeviceHardware;

public record DeviceHardwareUpdateCommand(
        DeviceId deviceId,
        DeviceHardware hardware
) {
}
