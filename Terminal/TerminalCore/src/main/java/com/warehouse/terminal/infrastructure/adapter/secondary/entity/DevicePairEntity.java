package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.device.Terminal;
import com.warehouse.terminal.domain.vo.DevicePairId;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "device_pair")
public class DevicePairEntity {

    @Id
    @Column(name = "device_pair_id", unique = true, nullable = false)
    private Long devicePairId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "device_id"))
    private DeviceId deviceId;

    @Column(name = "paired")
    private boolean paired;

    @Column(name = "pair_time")
    private Instant loginTime;

    @Column(name = "error_description")
    private String errorDescription;

    @Column(name = "pair_key")
    private String pairKey;

    @Column(name = "pair_key_expires_at")
    private Instant pairKeyExpiresAt;

    public DevicePairEntity() {
    }

    public DevicePairEntity(final Long devicePairId,
                            final DeviceId deviceId,
                            final boolean paired,
                            final Instant loginTime,
                            final String errorDescription,
                            final String pairKey,
                            final Instant pairKeyExpiresAt) {
        this.devicePairId = devicePairId;
        this.deviceId = deviceId;
        this.paired = paired;
        this.loginTime = loginTime;
        this.errorDescription = errorDescription;
        this.pairKey = pairKey;
        this.pairKeyExpiresAt = pairKeyExpiresAt;
    }

    public DevicePairEntity(final DeviceId deviceId) {
        this.deviceId = deviceId;
        this.paired = true;
        this.loginTime = Instant.now();
        this.errorDescription = "";
        this.pairKey = "pairKey";
    }

    public DevicePairEntity(final DevicePairId devicePairId,
                            final DeviceId deviceId,
                            final boolean paired,
                            final Instant loginTime,
                            final String errorDescription,
                            final String pairKey,
                            final Instant pairKeyExpiresAt) {
        this.devicePairId = devicePairId.value();
        this.deviceId = deviceId;
        this.paired = paired;
        this.loginTime = loginTime;
        this.errorDescription = errorDescription;
        this.pairKey = pairKey;
        this.pairKeyExpiresAt = pairKeyExpiresAt;
    }

    public DevicePairEntity(final DeviceId deviceId, final String errorDescription) {
        this.deviceId = deviceId;
        this.paired = false;
        this.loginTime = Instant.now();
        this.errorDescription = errorDescription;
    }

    public static DevicePairEntity from(final DevicePair devicePair, final Terminal terminal) {
        return new DevicePairEntity(
                devicePair.getDevicePairId(),
                terminal.getDeviceId(),
                devicePair.isPaired(),
                devicePair.getLoginTime(),
                devicePair.getErrorDescription(),
                devicePair.getPairKey(),
                devicePair.getPairKeyExpiresAt());
    }

    public static DevicePairEntity from(final DevicePair devicePair) {
        return new DevicePairEntity(
                devicePair.getDevicePairId(),
                devicePair.getDeviceId(),
                devicePair.isPaired(),
                devicePair.getLoginTime(),
                devicePair.getErrorDescription(),
                devicePair.getPairKey(),
                devicePair.getPairKeyExpiresAt());
    }

    public Long getDevicePairId() {
        return devicePairId;
    }

    public void setDevicePairId(final Long devicePairId) {
        this.devicePairId = devicePairId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean isPaired() {
        return paired;
    }

    public void setPaired(final boolean paired) {
        this.paired = paired;
    }

    public Instant getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(final Instant loginTime) {
        this.loginTime = loginTime;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(final String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getPairKey() {
        return pairKey;
    }

    public Instant getPairKeyExpiresAt() {
        return pairKeyExpiresAt;
    }

    public void pair() {
        this.paired = true;
    }
}
