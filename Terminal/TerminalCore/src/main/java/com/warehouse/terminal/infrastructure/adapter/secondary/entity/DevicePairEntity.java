package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.vo.DevicePairId;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "device_pair")
public class DevicePairEntity {

    @Id
    @Column(name = "device_pair_id", nullable = false)
    private Long devicePairId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "device_id"))
    private DeviceEntity device;

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

	public DevicePairEntity(final Long devicePairId, final DeviceEntity device, final boolean paired,
                            final Instant loginTime, final String errorDescription, final String pairKey) {
		this.devicePairId = devicePairId;
		this.device = device;
		this.paired = paired;
		this.loginTime = loginTime;
		this.errorDescription = errorDescription;
        this.pairKey = pairKey;
	}

    public DevicePairEntity(final DeviceEntity device) {
        this.device = device;
        this.paired = true;
        this.loginTime = Instant.now();
        this.errorDescription = "";
        this.pairKey = "pairKey";
    }

	public DevicePairEntity(final DevicePairId devicePairId,
                              final DeviceEntity device,
                              final boolean paired,
                              final Instant loginTime,
                              final String errorDescription,
                              final String pairKey) {
        this.devicePairId = devicePairId.value();
        this.device = device;
        this.paired = paired;
        this.loginTime = loginTime;
        this.errorDescription = errorDescription;
        this.pairKey = pairKey;
    }

    public DevicePairEntity(final DeviceEntity device, final String errorDescription) {
        this.device = device;
        this.paired = false;
        this.loginTime = Instant.now();
        this.errorDescription = errorDescription;
    }

    public static DevicePairEntity from(final DevicePair devicePair) {
        return new DevicePairEntity(devicePair.getDevicePairId(), new DeviceEntity(devicePair.getDeviceId()),
                devicePair.isPaired(), devicePair.getLoginTime(), devicePair.getErrorDescription(),
                devicePair.getPairKey());
    }

    public Long getDevicePairId() {
        return devicePairId;
    }

    public void setDevicePairId(final Long devicePairId) {
        this.devicePairId = devicePairId;
    }

    public DeviceEntity getDevice() {
        return device;
    }

    public void setDevice(final DeviceEntity deviceEntity) {
        this.device = deviceEntity;
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

