package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import com.warehouse.routelogger.domain.model.DeviceInformationRequest;

public record DeviceInformationRequestDto(ShipmentIdDto shipmentId,
                                          DeviceIdDto deviceId, DepartmentCodeDto departmentCode, DeviceTypeDto deviceType, DeviceVersionDto deviceVersion, UserIdDto userId,
                                          String processType) {

    public static DeviceInformationRequestDto from(final DeviceInformationRequest request) {
        final ShipmentIdDto shipmentId = new ShipmentIdDto(request.getShipmentId().value());
        final DeviceIdDto deviceId = new DeviceIdDto(request.getDeviceId().getValue());
        final DepartmentCodeDto departmentCode = new DepartmentCodeDto(request.getDepartmentCode().getValue());
        final DeviceTypeDto deviceType = DeviceTypeDto.valueOf(request.getDeviceType().name());
        final DeviceVersionDto deviceVersion = new DeviceVersionDto(request.getVersion());
        final UserIdDto userId = new UserIdDto(request.getUserId().getValue());
        return new DeviceInformationRequestDto(shipmentId, deviceId, departmentCode, deviceType, deviceVersion, userId, request.getProcessType().name());
    }
}
