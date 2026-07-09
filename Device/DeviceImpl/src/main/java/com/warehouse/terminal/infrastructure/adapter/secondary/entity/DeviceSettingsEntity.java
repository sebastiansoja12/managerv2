package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceSettings;
import jakarta.persistence.*;

@Entity
@Table(name = "device_settings")
public class DeviceSettingsEntity {

    @Id
    @Column(name = "device_settings_id", nullable = false, updatable = false)
    private String deviceSettingsId;

    @Column(name = "device_id", nullable = false, unique = true)
    @AttributeOverride(name = "value", column = @Column(name = "device_id"))
    private DeviceId deviceId;

    @Column(name = "cross_courier_delivery", nullable = false)
    private Boolean crossCourierDelivery;

    @Column(name = "validate_responsible_user", nullable = false)
    private Boolean validateResponsibleUser;

    @Column(name = "validate_department_code", nullable = false)
    private Boolean validateDepartmentCode;

    public DeviceSettingsEntity() {
    }

    public DeviceSettingsEntity(final String deviceSettingsId,
                                final DeviceId deviceId,
                                final Boolean crossCourierDelivery,
                                final Boolean validateResponsibleUser,
                                final Boolean validateDepartmentCode) {
        this.deviceSettingsId = deviceSettingsId;
        this.deviceId = deviceId;
        this.crossCourierDelivery = crossCourierDelivery;
        this.validateResponsibleUser = validateResponsibleUser;
        this.validateDepartmentCode = validateDepartmentCode;
    }

    public static DeviceSettingsEntity from(final DeviceSettings deviceSettings, final String deviceSettingsId) {
        return new DeviceSettingsEntity(deviceSettingsId, deviceSettings.getDeviceId(), deviceSettings.getCrossCourierDelivery(),
                deviceSettings.getValidateDepartmentCode(), deviceSettings.getValidateResponsibleUser());
    }

    public String getDeviceSettingsId() {
        return deviceSettingsId;
    }

    public void setDeviceSettingsId(final String deviceSettingsId) {
        this.deviceSettingsId = deviceSettingsId;
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
}
