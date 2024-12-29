package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DeviceVersion;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "device_version")
public class DeviceVersionEntity {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "device_id", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "device_id"))
    private DeviceId deviceId;

    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;

    public DeviceVersionEntity() {
    }

    public DeviceVersionEntity(final Long id, final DeviceType deviceType, final String version, final DeviceId deviceId) {
        this.id = id;
        this.deviceType = deviceType;
        this.version = version;
        this.deviceId = deviceId;
        this.lastUpdate = Instant.now();
    }

	public static DeviceVersionEntity from(final DeviceVersion deviceVersion) {
		return new DeviceVersionEntity(deviceVersion.getId(), deviceVersion.getDeviceType(), deviceVersion.getVersion(),
				deviceVersion.getDeviceId());
	}

    public Instant getLastUpdate() {
        return lastUpdate;
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

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }
}
