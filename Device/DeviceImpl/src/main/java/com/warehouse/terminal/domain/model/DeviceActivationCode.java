package com.warehouse.terminal.domain.model;

import java.time.Duration;
import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.vo.DeviceActivationCodeId;

public class DeviceActivationCode {

    private final DeviceActivationCodeId activationCodeId;
    private final DeviceId deviceId;
    private final UserId userId;
    private final OperatorId operatorId;
    private final DepartmentCode departmentCode;
    private final String codeHash;
    private final Instant createdAt;
    private final Instant expiresAt;
    private final Instant usedAt;

    public DeviceActivationCode(final DeviceActivationCodeId activationCodeId,
                                final DeviceId deviceId,
                                final UserId userId,
                                final OperatorId operatorId,
                                final DepartmentCode departmentCode,
                                final String codeHash,
                                final Instant createdAt,
                                final Instant expiresAt,
                                final Instant usedAt) {
        this.activationCodeId = activationCodeId;
        this.deviceId = deviceId;
        this.userId = userId;
        this.operatorId = operatorId;
        this.departmentCode = departmentCode;
        this.codeHash = codeHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.usedAt = usedAt;
    }

    public static DeviceActivationCode issue(final DeviceId deviceId,
                                             final String codeHash,
                                             final Instant timestamp,
                                             final Duration validity) {
        return new DeviceActivationCode(DeviceActivationCodeId.generate(), deviceId, null, null, null, codeHash,
                timestamp, timestamp.plus(validity), null);
    }

    public static DeviceActivationCode issueForUser(final UserId userId,
                                                    final OperatorId operatorId,
                                                    final DepartmentCode departmentCode,
                                                    final String codeHash,
                                                    final Instant timestamp,
                                                    final Duration validity) {
        return new DeviceActivationCode(DeviceActivationCodeId.generate(), null, userId, operatorId, departmentCode,
                codeHash, timestamp, timestamp.plus(validity), null);
    }

    public boolean isUsable(final Instant timestamp) {
        return usedAt == null && timestamp.isBefore(expiresAt);
    }

    public boolean isUserPairing() {
        return deviceId == null && userId != null;
    }

    public DeviceActivationCodeId getActivationCodeId() {
        return activationCodeId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public UserId getUserId() {
        return userId;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getUsedAt() {
        return usedAt;
    }
}
