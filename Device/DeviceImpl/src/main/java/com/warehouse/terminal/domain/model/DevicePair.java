package com.warehouse.terminal.domain.model;

import java.time.Instant;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.vo.DevicePairId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

public class DevicePair {

	private DevicePairId devicePairId;

	private DeviceId deviceId;

	private boolean paired;

	private Instant loginTime;

	private String errorDescription;

	private String pairKey;

	private Instant pairKeyExpiresAt;

	private UserId operatorUserId;

	private Instant operatorSessionExpiresAt;

	private Instant lastSeenAt;

	public DevicePair(final DevicePairId devicePairId,
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

	public DevicePair(final DeviceId deviceId,
					  final String pairKey,
					  final Instant pairKeyExpiresAt,
					  final DevicePairId devicePairId) {
		this.pairKey = pairKey;
		this.pairKeyExpiresAt = pairKeyExpiresAt;
		this.paired = true;
		this.deviceId = deviceId;
		this.loginTime = Instant.now();
		this.errorDescription = StringUtils.EMPTY;
		this.devicePairId = devicePairId;
	}

	public DevicePair(final boolean paired) {
		this.paired = paired;
	}

	public String getPairKey() {
		return pairKey;
	}

	public void setPairKey(final String pairKey) {
		this.pairKey = pairKey;
	}

	public Instant getPairKeyExpiresAt() {
		return pairKeyExpiresAt;
	}

	public void setPairKeyExpiresAt(final Instant pairKeyExpiresAt) {
		this.pairKeyExpiresAt = pairKeyExpiresAt;
	}

	public DevicePairId getDevicePairId() {
		return devicePairId;
	}

	public void setDevicePairId(final DevicePairId devicePairId) {
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

	public void pair(final String pairKey, final Instant pairKeyExpiresAt) {
		this.pairKey = pairKey;
		this.pairKeyExpiresAt = pairKeyExpiresAt;
		pair();
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
        final DevicePairId pairId = new DevicePairId(devicePairEntity.getDevicePairId());
        final DevicePair devicePair = new DevicePair(pairId, devicePairEntity.getDeviceId(),
                devicePairEntity.isPaired(), devicePairEntity.getLoginTime(),
                devicePairEntity.getErrorDescription(),
                devicePairEntity.getPairKey(),
				devicePairEntity.getPairKeyExpiresAt());
		devicePair.operatorUserId = devicePairEntity.getOperatorUserId();
		devicePair.operatorSessionExpiresAt = devicePairEntity.getOperatorSessionExpiresAt();
		devicePair.lastSeenAt = devicePairEntity.getLastSeenAt();
        return devicePair;
    }

	public void activate(final String pairKey, final Instant pairKeyExpiresAt) {
		pair(pairKey, pairKeyExpiresAt);
		endOperatorSession();
	}

	public void startOperatorSession(final UserId userId, final Instant expiresAt) {
		this.operatorUserId = userId;
		this.operatorSessionExpiresAt = expiresAt;
	}

	public void endOperatorSession() {
		this.operatorUserId = null;
		this.operatorSessionExpiresAt = null;
	}

	public boolean isOperatorSessionOwnedBy(final UserId userId) {
		return operatorUserId != null && operatorUserId.equals(userId);
	}

	public Optional<UserId> activeOperator(final Instant now) {
		if (operatorUserId == null || operatorSessionExpiresAt == null || !operatorSessionExpiresAt.isAfter(now)) {
			return Optional.empty();
		}
		return Optional.of(operatorUserId);
	}

	public void markSeen(final Instant now) {
		this.lastSeenAt = now;
	}

	public UserId getOperatorUserId() {
		return operatorUserId;
	}

	public Instant getOperatorSessionExpiresAt() {
		return operatorSessionExpiresAt;
	}

	public Instant getLastSeenAt() {
		return lastSeenAt;
	}

	public boolean containsValidPairKey(final Instant now) {
		return StringUtils.isNotBlank(pairKey)
				&& pairKeyExpiresAt != null
				&& pairKeyExpiresAt.isAfter(now);
	}
}
