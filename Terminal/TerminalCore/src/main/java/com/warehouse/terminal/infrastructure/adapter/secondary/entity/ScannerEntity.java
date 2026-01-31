package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.device.Scanner;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "scanners")
public class ScannerEntity {

    public DeviceId getId() {
        return deviceId;
    }

    public void setId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "device_id"))
    private DeviceId deviceId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "external_device_id"))
    private ExternalId<String> externalDeviceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeviceStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_code"))
    private DepartmentCode departmentCode;

    @Embedded
    private Identity identity;

    @Embedded
    private Hardware hardware;

    @Embedded
    private Network network;

    @Embedded
    private Ownership ownership;

    @Enumerated(EnumType.STRING)
    @Column(name = "scan_type")
    private Scanner.ScanType scanType;

    @Enumerated(EnumType.STRING)
    @Column(name = "scanner_type")
    private Scanner.ScannerType scannerType;

    @PrePersist
    void ensureDefaults() {
        if (this.deviceType == null) {
            this.deviceType = DeviceType.SCANNER;
        }
        if (this.status == null) {
            this.status = DeviceStatus.ACTIVE;
        }
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = Instant.now();
        }
    }
}
