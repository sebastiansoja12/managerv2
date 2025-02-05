package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceSettingsEntity;

public class DeviceSettings {
    private DeviceId deviceId;
    private Boolean crossCourierDelivery;
    private Boolean validateResponsibleUser;
    private Boolean validateDepartmentCode;

    public DeviceSettings() {
    }

    public DeviceSettings(final DeviceId deviceId,
                          final Boolean crossCourierDelivery,
                          final Boolean validateResponsibleUser,
                          final Boolean validateDepartmentCode) {
        this.deviceId = deviceId;
        this.crossCourierDelivery = crossCourierDelivery;
        this.validateResponsibleUser = validateResponsibleUser;
        this.validateDepartmentCode = validateDepartmentCode;
    }

    public static DeviceSettings empty() {
        return new DeviceSettings();
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getCrossCourierDelivery() {
        return crossCourierDelivery;
    }

    public void setCrossCourierDelivery(final Boolean crossCourierDelivery) {
        this.crossCourierDelivery = crossCourierDelivery;
    }

    public Boolean getValidateResponsibleUser() {
        return validateResponsibleUser;
    }

    public void setValidateResponsibleUser(final Boolean validateResponsibleUser) {
        this.validateResponsibleUser = validateResponsibleUser;
    }

    public Boolean getValidateDepartmentCode() {
        return validateDepartmentCode;
    }

    public void setValidateDepartmentCode(final Boolean validateDepartmentCode) {
        this.validateDepartmentCode = validateDepartmentCode;
    }

    public static DeviceSettings from(final DeviceSettingsEntity deviceSettingsEntity) {
        return new DeviceSettings(deviceSettingsEntity.getDeviceId(), deviceSettingsEntity.getCrossCourierDelivery(),
                deviceSettingsEntity.getValidateResponsibleUser(), deviceSettingsEntity.getValidateDepartmentCode());
    }
}
