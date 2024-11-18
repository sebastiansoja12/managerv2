package com.warehouse.terminal.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceVersionEntity;

public class DeviceVersion {

    private Long id;

    private DeviceType deviceType;

    private String version;

    private DeviceId deviceId;

    private Instant lastUpdate;

	public DeviceVersion(final Long id,
                         final DeviceType deviceType,
                         final String version,
                         final DeviceId deviceId,
                         final Instant lastUpdate) {
        this.id = id;
        this.deviceType = deviceType;
        this.version = version;
        this.deviceId = deviceId;
        this.lastUpdate = lastUpdate;
    }

    public DeviceVersion(final String version, final DeviceId deviceId) {
        this.version = version;
        this.deviceId = deviceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void updateDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
        markAsModified();
    }

    public String getVersion() {
        return version;
    }

    public void updateVersion(final String version) {
        this.version = version;
        markAsModified();
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void markAsModified() {
        this.lastUpdate = Instant.now();
    }


	public static DeviceVersion from(final DeviceVersionEntity deviceVersionEntity) {
		return new DeviceVersion(deviceVersionEntity.getId(), deviceVersionEntity.getDeviceType(),
				deviceVersionEntity.getVersion(), new DeviceId(deviceVersionEntity.getId()),
				deviceVersionEntity.getLastUpdate());
	}
}
