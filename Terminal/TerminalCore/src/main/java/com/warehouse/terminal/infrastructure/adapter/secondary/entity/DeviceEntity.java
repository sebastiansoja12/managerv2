package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.Terminal;

import jakarta.persistence.*;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "active", nullable = false)
    private Boolean active;

	public DeviceEntity(final Long deviceId, final String version, final Long userId, final String depotCode,
                        final DeviceType deviceType, final Boolean active) {
		this.deviceId = deviceId;
		this.version = version;
		this.userId = userId;
		this.depotCode = depotCode;
		this.deviceType = deviceType;
        this.active = active;
    }

    public DeviceEntity(final Long value) {
        this.deviceId = value;
    }

    public static DeviceEntity from(final Terminal terminal) {
		return new DeviceEntity(terminal.getTerminalId().getValue(), terminal.getVersion(),
				terminal.getUserId().getValue(), terminal.getDepotCode(), terminal.getDeviceType(), terminal.isActive());
	}

    public Boolean getActive() {
        return active;
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
