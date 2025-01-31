package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.Username;
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

    @Column(name = "active", nullable = false, updatable = false, unique = true)
    @AttributeOverride(name = "value", column = @Column(name = "username"))
    private Username username;

    public DeviceEntity() {
    }

    public DeviceEntity(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceEntity(final DeviceId deviceId,
                        final String version,
                        final DepartmentCode departmentCode,
                        final DeviceType deviceType,
                        final Boolean active,
                        final Username username) {
        this.deviceId = deviceId;
        this.version = version;
        this.departmentCode = departmentCode;
        this.deviceType = deviceType;
        this.lastUpdate = Instant.now();
        this.active = active;
        this.username = username;
    }

    public static DeviceEntity from(final Terminal terminal) {
        return new DeviceEntity(terminal.getDeviceId(), terminal.getVersion(),
                new DepartmentCode(terminal.getDepartmentCode()), terminal.getDeviceType(), terminal.isActive(),
                terminal.getUsername());
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

    public Username getUsername() {
        return username;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }
}
