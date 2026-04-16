package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.Username;

public record DeviceUserRequest(DeviceId deviceId, Username username) {
}
