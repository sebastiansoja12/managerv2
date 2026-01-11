package com.warehouse.process.infrastructure.dto;

public record DeviceInformationDto(DeviceIdDto deviceId,
                                   DepartmentCodeDto departmentCode,
                                   UserIdDto userId,
                                   DeviceTypeDto deviceType,
                                   DeviceUserTypeDto deviceUserType,
                                   String version) {
}
