package com.warehouse.terminal.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

public class DevicePair {

	private Long devicePairId;

	private DeviceId deviceId;

	private boolean paired;

	private Instant loginTime;

	private String errorDescription;

	private String pairKey;

	public DevicePair(final Long devicePairId, final DeviceId deviceId, final boolean paired, final Instant loginTime,
                      final String errorDescription, String pairKey) {
		this.devicePairId = devicePairId;
		this.deviceId = deviceId;
		this.paired = paired;
		this.loginTime = loginTime;
		this.errorDescription = errorDescription;
        this.pairKey = pairKey;
    }

	public String getPairKey() {
		return pairKey;
	}

	public void setPairKey(String pairKey) {
		this.pairKey = pairKey;
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

	public boolean isPaired() {
		return paired;
	}

	public void pair() {
		this.paired = true;
        updateLoginTime();
    }

    public void unpair() {
        this.paired = false;
    }

	public Instant getLoginTime() {
		return loginTime;
	}

	public void updateLoginTime() {
		this.loginTime = Instant.now();
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(final String errorDescription) {
		this.errorDescription = errorDescription;
	}

    public static DevicePair from(final DevicePairEntity devicePairEntity) {
        return new DevicePair(devicePairEntity.getDevicePairId(),
                new DeviceId(devicePairEntity.getDevicePairId()),
                devicePairEntity.isPaired(), devicePairEntity.getLoginTime(),
                devicePairEntity.getErrorDescription(),
				devicePairEntity.getPairKey());
    }
}
