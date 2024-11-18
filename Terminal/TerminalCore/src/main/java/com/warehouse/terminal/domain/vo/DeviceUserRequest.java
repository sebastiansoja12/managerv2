package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;

public record DeviceUserRequest(DeviceId deviceId, UserId userId) {
}
