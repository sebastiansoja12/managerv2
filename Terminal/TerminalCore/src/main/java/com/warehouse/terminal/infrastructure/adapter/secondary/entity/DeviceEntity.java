package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.Terminal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "device")
public class DeviceEntity {

    @Id
    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "depot_code", nullable = false)
    private String depotCode;

    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

	public DeviceEntity(final Long deviceId, final String version, final Long userId, final String depotCode,
			final DeviceType deviceType) {
		this.deviceId = deviceId;
		this.version = version;
		this.userId = userId;
		this.depotCode = depotCode;
		this.deviceType = deviceType;
	}

	public static DeviceEntity from(final Terminal terminal) {
		return new DeviceEntity(terminal.getTerminalId().getValue(), terminal.getVersion(),
				terminal.getUserId().getValue(), terminal.getDepotCode(), terminal.getDeviceType());
	}

    public Long getDeviceId() {
        return deviceId;
    }

    public String getVersion() {
        return version;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
}
