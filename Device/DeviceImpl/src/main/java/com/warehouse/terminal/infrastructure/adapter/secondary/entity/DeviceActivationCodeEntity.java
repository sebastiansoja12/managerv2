package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import java.time.Instant;
import java.util.UUID;

import com.warehouse.commonassets.identificator.DeviceId;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "device_activation_codes")
public class DeviceActivationCodeEntity {

    @Id
    @Column(name = "activation_code_id", nullable = false)
    private UUID activationCodeId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "device_id"))
    private DeviceId deviceId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "code_hash", nullable = false, length = 64)
    private String codeHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;

    protected DeviceActivationCodeEntity() {
    }

    public DeviceActivationCodeEntity(final UUID activationCodeId,
                                      final DeviceId deviceId,
                                      final Long userId,
                                      final Long operatorId,
                                      final String departmentCode,
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

    public UUID getActivationCodeId() {
        return activationCodeId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public String getDepartmentCode() {
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
