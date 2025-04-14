package com.warehouse.terminal.dto;

public record DeviceSettingsRequestDto(DeviceIdDto deviceId, Boolean crossCourierDelivery,
                                       Boolean validateResponsibleUser, Boolean validateDepartmentCode) {

    public Long deviceIdValue() {
        return deviceId.value();
    }
}
