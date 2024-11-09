package com.warehouse.terminal.domain.vo;

public class DeviceInformation {

    private final String version;

    private final Long deviceId;

    private final Long userId;

    private final String depotCode;

    public DeviceInformation(final String version, final Long deviceId, final Long userId, final String depotCode) {
        this.version = version;
        this.deviceId = deviceId;
        this.userId = userId;
        this.depotCode = depotCode;
    }

    public String getVersion() {
        return version;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDepotCode() {
        return depotCode;
    }
}

