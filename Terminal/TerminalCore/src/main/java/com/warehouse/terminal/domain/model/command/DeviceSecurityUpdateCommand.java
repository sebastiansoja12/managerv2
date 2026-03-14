package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.vo.SecurityProfile;

public record DeviceSecurityUpdateCommand(
        DeviceId deviceId,
        SecurityProfile security
) {
}
