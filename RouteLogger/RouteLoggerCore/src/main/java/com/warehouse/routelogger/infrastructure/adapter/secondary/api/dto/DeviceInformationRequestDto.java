package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import com.warehouse.routelogger.domain.model.DeviceInformationRequest;

public record DeviceInformationRequestDto(DeviceIdDto deviceId, DepartmentCodeDto departmentCode, DeviceTypeDto deviceType, DeviceVersionDto deviceVersion, UserIdDto userId) {

    public static DeviceInformationRequestDto from(final DeviceInformationRequest request) {
        final DeviceIdDto deviceId = new DeviceIdDto(request.getDeviceId().getValue());
        final DepartmentCodeDto departmentCode = new DepartmentCodeDto(request.getDepartmentCode().getValue());
        final DeviceTypeDto deviceType = DeviceTypeDto.valueOf(request.getDeviceType().name());
        final DeviceVersionDto deviceVersion = new DeviceVersionDto(request.getVersion());
        final UserIdDto userId = new UserIdDto(request.getUserId().getValue());
        return new DeviceInformationRequestDto(deviceId, departmentCode, deviceType, deviceVersion, userId);
    }
}
