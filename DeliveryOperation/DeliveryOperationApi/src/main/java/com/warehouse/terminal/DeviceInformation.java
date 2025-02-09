package com.warehouse.terminal;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.information.Device;

public class DeviceInformation {
    private final DeviceId deviceId;
    private final DepartmentCode departmentCode;
    private final DeviceType deviceType;
    private final DeviceUserType deviceUserType;
    private final String version;
    private final String username;
    private final boolean updateRequired;

    private DeviceInformation(final Builder builder) {
        this.deviceId = builder.deviceId;
        this.departmentCode = builder.departmentCode;
        this.deviceType = builder.deviceType;
        this.deviceUserType = builder.deviceUserType;
        this.version = builder.version;
        this.username = builder.username;
        this.updateRequired = builder.updateRequired;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static DeviceInformation from(final Device device) {
        return DeviceInformation.builder()
                .departmentCode(new DepartmentCode(device.getDepartmentCode()))
                .deviceId(new DeviceId(device.getDeviceId()))
                .username(device.getUsername())
                .version(device.getVersion())
                .deviceType(DeviceType.valueOf(device.getDeviceType()))
                .deviceUserType(DeviceUserType.valueOf(device.getDeviceUserType()))
                .build();
    }

    public static class Builder {
        private DeviceId deviceId;
        private DepartmentCode departmentCode;
        private DeviceType deviceType;
        private DeviceUserType deviceUserType;
        private String version;
        private String username;
        private boolean updateRequired;

        public Builder deviceId(final DeviceId deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder departmentCode(final DepartmentCode departmentCode) {
            this.departmentCode = departmentCode;
            return this;
        }

        public Builder deviceType(final DeviceType deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public Builder deviceUserType(final DeviceUserType deviceUserType) {
            this.deviceUserType = deviceUserType;
            return this;
        }

        public Builder version(final String version) {
            this.version = version;
            return this;
        }

        public Builder username(final String username) {
            this.username = username;
            return this;
        }

        public Builder updateRequired(final boolean updateRequired) {
            this.updateRequired = updateRequired;
            return this;
        }

        public DeviceInformation build() {
            return new DeviceInformation(this);
        }
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public DeviceUserType getDeviceUserType() {
        return deviceUserType;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public String getVersion() {
        return version;
    }

    public String getUsername() {
        return username;
    }

    public boolean isUpdateRequired() {
        return updateRequired;
    }
}

