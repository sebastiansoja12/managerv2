package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import com.warehouse.commonassets.enumeration.DeviceType;
import jakarta.persistence.*;

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
    private String deviceId;

    public DeviceVersionEntity() {
    }

    public DeviceVersionEntity(final Long id, final DeviceType deviceType, final String version, final String deviceId) {
        this.id = id;
        this.deviceType = deviceType;
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

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }
}
