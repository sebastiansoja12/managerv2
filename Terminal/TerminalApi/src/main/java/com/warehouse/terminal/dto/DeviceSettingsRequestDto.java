package com.warehouse.terminal.dto;

public record DeviceSettingsRequestDto(DeviceIdDto deviceId, Boolean crossCourierDelivery,
                                       Boolean validateResponsibleUser, Boolean validateDepartmentCode) {

    public String deviceIdValue() {
        return deviceId.value();
    }
}
