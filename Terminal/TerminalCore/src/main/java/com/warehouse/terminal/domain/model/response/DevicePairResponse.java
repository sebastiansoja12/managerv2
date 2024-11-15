package com.warehouse.terminal.domain.model.response;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.vo.DevicePairId;

public record DevicePairResponse(UserId userId, DeviceId deviceId, DevicePairId devicePairId,
                                 String pairStatus, String pairKey, Boolean userValid, Boolean deviceValid,
                                 Boolean deviceUpToDate) {
}
