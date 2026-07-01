package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;

public record DeviceExternalDeviceIdUpdateCommand(
        DeviceId deviceId,
        ExternalId<String> externalDeviceId
) {
}
