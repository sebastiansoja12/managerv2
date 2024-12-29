package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Terminal;

import jakarta.persistence.*;

@Entity
@Table(name = "device")
public class DeviceEntity {

    @Column(name = "device_id")
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "device_id"))
    private DeviceId deviceId;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "user_id", nullable = false)
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @Column(name = "department_code", nullable = false)
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_code"))
    private DepartmentCode departmentCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public DeviceEntity() {
    }

    public DeviceEntity(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceEntity(final DeviceId deviceId,
                        final String version,
                        final UserId userId,
                        final DepartmentCode departmentCode,
                        final DeviceType deviceType,
                        final Boolean active) {
        this.deviceId = deviceId;
		this.version = version;
		this.userId = userId;
		this.departmentCode = departmentCode;
		this.deviceType = deviceType;
        this.lastUpdate = Instant.now();
        this.active = active;
    }

    public static DeviceEntity from(final Terminal terminal) {
		return new DeviceEntity(terminal.getDeviceId(), terminal.getVersion(),
				terminal.getUserId(), new DepartmentCode(terminal.getDepartmentCode()),
                terminal.getDeviceType(), terminal.isActive());
	}

    public Boolean isActive() {
        return active;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public String getVersion() {
        return version;
    }

    public UserId getUserId() {
        return userId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
}
