package com.warehouse.terminal.domain.model.request;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.dto.DeviceSettingsRequestDto;

public class DeviceSettingsRequest {
    private DeviceId deviceId;
    private Boolean enableCrossCourierDelivery;
    private Boolean validateResponsibleUser;
    private Boolean validateDepartmentCode;

    public DeviceSettingsRequest(final DeviceId deviceId,
            final Boolean enableCrossCourierDelivery,
            final Boolean validateResponsibleUser,
            final Boolean validateDepartmentCode) {
        this.deviceId = deviceId;
        this.enableCrossCourierDelivery = enableCrossCourierDelivery;
        this.validateResponsibleUser = validateResponsibleUser;
        this.validateDepartmentCode = validateDepartmentCode;
    }

    public static DeviceSettingsRequest from(final DeviceSettingsRequestDto request) {
        final DeviceId deviceId = new DeviceId(request.deviceIdValue());
        return new DeviceSettingsRequest(deviceId, request.crossCourierDelivery(),
                request.validateResponsibleUser(), request.validateDepartmentCode());
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getEnableCrossCourierDelivery() {
        return enableCrossCourierDelivery;
    }

    public void setEnableCrossCourierDelivery(final Boolean enableCrossCourierDelivery) {
        this.enableCrossCourierDelivery = enableCrossCourierDelivery;
    }

    public Boolean isValidateResponsibleUser() {
        return validateResponsibleUser;
    }

    public void setValidateResponsibleUser(Boolean validateResponsibleUser) {
        this.validateResponsibleUser = validateResponsibleUser;
    }

    public Boolean isValidateDepartmentCode() {
        return validateDepartmentCode;
    }

    public void setValidateDepartmentCode(Boolean validateDepartmentCode) {
        this.validateDepartmentCode = validateDepartmentCode;
    }
}
