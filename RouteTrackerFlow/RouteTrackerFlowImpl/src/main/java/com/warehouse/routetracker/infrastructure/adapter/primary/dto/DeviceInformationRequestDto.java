package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public record DeviceInformationRequestDto(DeviceIdDto deviceId, DepartmentIdDto departmentId,
                                          DeviceTypeDto deviceType, DeviceVersionDto deviceVersion,
                                          UserIdDto userId, ShipmentIdDto shipmentId,
                                          ProcessTypeDto processType) {

}
