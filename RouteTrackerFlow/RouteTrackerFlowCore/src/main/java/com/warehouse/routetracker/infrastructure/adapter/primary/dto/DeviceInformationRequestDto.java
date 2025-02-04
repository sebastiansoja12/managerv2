package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public record DeviceInformationRequestDto(DeviceIdDto deviceId, DepartmentCodeDto departmentCode, DeviceTypeDto deviceType, DeviceVersionDto deviceVersion, UsernameDto username,
                                          ShipmentIdDto shipmentId, ProcessTypeDto processType) {

}

