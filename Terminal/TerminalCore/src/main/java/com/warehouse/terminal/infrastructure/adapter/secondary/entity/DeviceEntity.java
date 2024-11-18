package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.model.Terminal;

import jakarta.persistence.*;

@Entity
@Table(name = "device")
public class DeviceEntity {

    @Id
    private Long id;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "depot_code", nullable = false)
    private String depotCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public DeviceEntity() {
    }

    public DeviceEntity(final Long id) {
        this.id = id;
    }

    public DeviceEntity(final Long id,
                        final String version,
                        final Long userId,
                        final String depotCode,
                        final DeviceType deviceType,
                        final Boolean active) {
        this.id = id;
		this.version = version;
		this.userId = userId;
		this.depotCode = depotCode;
		this.deviceType = deviceType;
        this.lastUpdate = Instant.now();
        this.active = active;
    }

    public static DeviceEntity from(final Terminal terminal) {
		return new DeviceEntity(terminal.getDeviceId().getValue(), terminal.getVersion(),
				terminal.getUserId().getValue(), terminal.getDepotCode(), terminal.getDeviceType(), terminal.isActive());
	}

    public Boolean isActive() {
        return active;
    }

    public Long getDeviceId() {
        return id;
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
