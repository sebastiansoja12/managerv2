package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import java.time.Instant;

import com.warehouse.terminal.domain.model.DevicePair;
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

    @Column(name = "pair_key")
    private String pairKey;

    public DevicePairEntity() {
    }

	public DevicePairEntity(final Long devicePairId, final DeviceEntity deviceEntity, final boolean paired,
			final Instant loginTime, final String errorDescription, final String pairKey) {
		this.devicePairId = devicePairId;
		this.deviceEntity = deviceEntity;
		this.paired = paired;
		this.loginTime = loginTime;
		this.errorDescription = errorDescription;
        this.pairKey = pairKey;
	}

    public DevicePairEntity(final DeviceEntity deviceEntity) {
        this.deviceEntity = deviceEntity;
        this.paired = true;
        this.loginTime = Instant.now();
        this.errorDescription = "";
        this.pairKey = "pairKey";
    }

	public DevicePairEntity(final DevicePairId devicePairId,
                              final DeviceEntity deviceEntity,
                              final boolean paired,
                              final Instant loginTime,
                              final String errorDescription,
                              final String pairKey) {
        this.devicePairId = devicePairId.value();
        this.deviceEntity = deviceEntity;
        this.paired = paired;
        this.loginTime = loginTime;
        this.errorDescription = errorDescription;
        this.pairKey = pairKey;
    }

    public DevicePairEntity(final DeviceEntity deviceEntity, final String errorDescription) {
        this.deviceEntity = deviceEntity;
        this.paired = false;
        this.loginTime = Instant.now();
        this.errorDescription = errorDescription;
    }

    public static DevicePairEntity from(final DevicePair devicePair) {
        return new DevicePairEntity(devicePair.getDevicePairId(), new DeviceEntity(devicePair.getDeviceId().getValue()),
                devicePair.isPaired(), devicePair.getLoginTime(), devicePair.getErrorDescription(),
                devicePair.getPairKey());
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

    public void pair() {
        this.paired = true;
    }
}

