package com.warehouse.terminal.dto;

public record DeviceValidationRequestDto(DeviceIdDto deviceId, DepartmentCodeDto departmentCode,
                                         UsernameDto username, VersionDto version, DeviceUserTypeDto deviceUserType,
                                         DeviceTypeDto deviceType, Boolean active) {
}
