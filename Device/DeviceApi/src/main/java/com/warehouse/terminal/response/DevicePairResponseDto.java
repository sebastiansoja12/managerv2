package com.warehouse.terminal.response;

import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.DevicePairIdDto;
import com.warehouse.terminal.dto.UserIdDto;

public record DevicePairResponseDto(UserIdDto userId,
                                    DeviceIdDto deviceId,
                                    DevicePairIdDto devicePairId,
                                    String pairStatus,
                                    String pairKey,
                                    Boolean userValid,
                                    Boolean deviceValid,
                                    Boolean deviceUpToDate) {
}
