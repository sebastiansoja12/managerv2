package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import java.time.Instant;

import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.vo.DevicePairId;

import jakarta.persistence.*;

@Entity
@Table(name = "device_pair")
public class DevicePairEntity {

    @Id
    @Column(name = "device_pair_id", nullable = false)
    private Long devicePairId;

    @OneToOne(fetch = FetchType.LAZY)
    private DeviceEntity deviceEntity;

    @Column(name = "paired")
    private boolean paired;

    @Column(name = "pair_time")
    private Instant loginTime;

    @Column(name = "error_description")
    private String errorDescription;

    public DevicePairEntity() {
    }

    public DevicePairEntity(final DeviceEntity deviceEntity) {
        this.deviceEntity = deviceEntity;
        this.paired = true;
        this.loginTime = Instant.now();
        this.errorDescription = "";
    }

	public DevicePairEntity(final DevicePairId devicePairId,
                              final DeviceEntity deviceEntity,
                              final boolean paired,
                              final Instant loginTime,
                              final String errorDescription) {
        this.devicePairId = devicePairId.value();
        this.deviceEntity = deviceEntity;
        this.paired = paired;
        this.loginTime = loginTime;
        this.errorDescription = errorDescription;
    }

    public DevicePairEntity(final DeviceEntity deviceEntity, final String errorDescription) {
        this.deviceEntity = deviceEntity;
        this.paired = false;
        this.loginTime = Instant.now();
        this.errorDescription = errorDescription;
    }

    public static DevicePairEntity from(final Terminal terminal) {
        return new DevicePairEntity();
    }

    public Long getDevicePairId() {
        return devicePairId;
    }

    public void setDevicePairId(final Long devicePairId) {
        this.devicePairId = devicePairId;
    }

    public DeviceEntity getDeviceEntity() {
        return deviceEntity;
    }

    public void setDeviceEntity(final DeviceEntity deviceEntity) {
        this.deviceEntity = deviceEntity;
    }

    public boolean isPaired() {
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
}

