package com.warehouse.terminal.response;

import com.warehouse.terminal.dto.DepartmentCodeDto;
import com.warehouse.terminal.dto.DeviceIdDto;
import com.warehouse.terminal.dto.UserIdDto;
import com.warehouse.terminal.dto.UsernameDto;

public record DeviceAuthenticationResponseDto(Boolean value,
                                              DeviceIdDto deviceId,
                                              DepartmentCodeDto departmentCode,
                                              UserIdDto userId,
                                              UsernameDto username) {

    public static DeviceAuthenticationResponseDto invalid() {
        return new DeviceAuthenticationResponseDto(false, null, null, null, null);
    }
}
