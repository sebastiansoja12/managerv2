package com.warehouse.terminal.dto;

public record DeviceValidationRequestDto(DeviceIdDto deviceId, DepartmentCodeDto departmentCode,
                                         UserIdDto userId, VersionDto version, DeviceUserTypeDto deviceUserType,
                                         DeviceTypeDto deviceType, Boolean active) {
}
