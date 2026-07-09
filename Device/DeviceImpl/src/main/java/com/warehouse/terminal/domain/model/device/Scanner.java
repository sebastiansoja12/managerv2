package com.warehouse.terminal.domain.model.device;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.ExecutionSourceType;
import com.warehouse.commonassets.helper.ExecutionSourceResolver;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DeviceHandler;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.command.DeviceUpdateCommand;
import com.warehouse.terminal.domain.vo.*;

public class Scanner implements Device, DeviceHandler, ExecutionSourceResolver {

    private DeviceId deviceId;
    private ExternalId<String> externalDeviceId;
    private DeviceType deviceType;
    private DeviceStatus status;
    private DeviceIdentity identity;
    private DeviceHardware hardware;
    private DeviceNetwork network;
    private OwnershipProfile ownership;
    private ScanType scanType;
    private ScannerType scannerType;
    private Instant createdAt;
    private Instant updatedAt;

    public Scanner(final DeviceId deviceId,
                   final DeviceIdentity identity,
                   final DeviceHardware hardware,
                   final DeviceNetwork network,
                   final OwnershipProfile ownership,
                   final ScanType scanType,
                   final ScannerType scannerType) {
        this.deviceId = deviceId;
        this.externalDeviceId = ExternalId.generateId();
        this.deviceType = DeviceType.SCANNER;
        this.status = DeviceStatus.ACTIVE;
        this.identity = identity;
        this.hardware = hardware;
        this.network = network;
        this.ownership = ownership;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.scanType = scanType;
        this.scannerType = scannerType;
    }

    public Scanner(final DeviceId deviceId,
                   final ExternalId<String> externalDeviceId,
                   final DeviceType deviceType,
                   final DeviceStatus status,
                   final DeviceIdentity identity,
                   final DeviceHardware hardware,
                   final DeviceNetwork network,
                   final OwnershipProfile ownership,
                   final Instant createdAt,
                   final Instant updatedAt,
                   final ScanType scanType,
                   final ScannerType scannerType) {
        this.deviceId = deviceId;
        this.externalDeviceId = externalDeviceId;
        this.deviceType = deviceType;
        this.status = status;
        this.identity = identity;
        this.hardware = hardware;
        this.network = network;
        this.ownership = ownership;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.scanType = scanType;
        this.scannerType = scannerType;
    }

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public ExternalId<String> getExternalDeviceId() {
        return externalDeviceId;
    }

    @Override
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @Override
    public DeviceStatus getStatus() {
        return status;
    }

    @Override
    public DeviceIdentity getIdentity() {
        return identity;
    }

    @Override
    public DeviceHardware getHardware() {
        return hardware;
    }

    @Override
    public DeviceSoftware getSoftware() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeviceNetwork getNetwork() {
        return network;
    }

    @Override
    public SecurityProfile getSecurity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeviceLocation getLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public OwnershipProfile getOwnership() {
        return ownership;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public DeviceSnapshot toSnapshot() {
        return new DeviceSnapshot(
                this.deviceId,
                this.externalDeviceId,
                this.deviceType,
                this.status,
                this.identity,
                this.hardware,
                null,
                this.network,
                null,
                null,
                this.ownership,
                null,
                this.createdAt,
                this.updatedAt,
                getLastUpdate(),
                isActive(),
                this.scanType,
                this.scannerType
        );
    }

    public ScanType getScanType() {
        return scanType;
    }

    @Override
    public UserId getUserId() {
        return this.ownership != null ? this.ownership.getUserId() : null;
    }

    @Override
    public DepartmentCode getDepartmentCode() {
        return this.ownership != null ? this.ownership.getDepartmentCode() : null;
    }

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant getLastUpdate() {
        return this.updatedAt;
    }

    @Override
    public Boolean isActive() {
        return DeviceStatus.ACTIVE.equals(this.status);
    }

    @Override
    public void update(final DeviceUpdateCommand request) {
        if (request.deviceType() != null) {
            updateDeviceType(request.deviceType());
        }
        if (request.active() != null) {
            updateActive(request.active());
        }
        if (request.status() != null) {
            updateStatus(request.status());
        }
        if (request.identity() != null) {
            updateIdentity(request.identity());
        }
        if (request.hardware() != null) {
            updateHardware(request.hardware());
        }
        if (request.software() != null) {
            updateSoftware(request.software());
        }
        if (request.network() != null) {
            updateNetwork(request.network());
        }
        if (request.security() != null) {
            updateSecurity(request.security());
        }
        if (request.location() != null) {
            updateLocation(request.location());
        }
        if (request.ownership() != null) {
            updateOwnership(request.ownership());
        }
        if (request.userId() != null) {
            assignUser(request.userId());
        }
        if (request.departmentCode() != null) {
            assignDepartmentCode(request.departmentCode());
        }
        if (request.version() != null) {
            updateVersion(request.version());
        }
    }

    public void setScanType(final ScanType scanType) {
        this.scanType = scanType;
    }

    public ScannerType getScannerType() {
        return scannerType;
    }

    public void setScannerType(final ScannerType scannerType) {
        this.scannerType = scannerType;
    }

    @Override
    public void assignDepartmentCode(final DepartmentCode departmentCode) {
        ensureOwnership().setDepartmentCode(departmentCode);
        markAsModified();
    }

    @Override
    public void updateStatus(final DeviceStatus status) {
        this.status = status;
        markAsModified();
    }

    @Override
    public void updateActive(final Boolean active) {
        if (Boolean.TRUE.equals(active)) {
            updateStatus(DeviceStatus.ACTIVE);
        } else if (Boolean.FALSE.equals(active)) {
            updateStatus(DeviceStatus.BLOCKED);
        }
    }

    @Override
    public void updateIdentity(final DeviceIdentity identity) {
        if (this.identity == null) {
            this.identity = identity;
        } else {
            this.identity.update(identity);
        }
        markAsModified();
    }

    @Override
    public void updateHardware(final DeviceHardware hardware) {
        if (this.hardware == null) {
            this.hardware = hardware;
        } else {
            this.hardware.update(hardware);
        }
        markAsModified();
    }

    @Override
    public void updateNetwork(final DeviceNetwork network) {
        this.network.update(network);
    }

    @Override
    public void updateOwnership(final OwnershipProfile ownership) {
        if (this.ownership == null) {
            this.ownership = ownership;
        } else {
            this.ownership.update(ownership);
        }
        markAsModified();
    }

    @Override
    public void assignUser(final UserId userId) {
        ensureOwnership().setUserId(userId);
        markAsModified();
    }

    @Override
    public void changeVersion(final String version) {
        updateVersion(version);
    }

    @Override
    public void updateVersion(final String version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
        markAsModified();
    }

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }

    private OwnershipProfile ensureOwnership() {
        if (this.ownership == null) {
            this.ownership = OwnershipProfile.initializeOwnership("", null, null, null);
        }
        return this.ownership;
    }

    @Override
    public boolean canHandle(final DeviceType deviceType) {
        return DeviceType.SCANNER.equals(deviceType);
    }

    @Override
    public ExecutionSourceType getExecutionSourceType() {
        return ExecutionSourceType.DEVICE;
    }

    public enum ScanType {
        BARCODE, QRCODE
    }

    public enum ScannerType {
        HANDHELD,
        WRIST_WORN,
        RING,
        GLOVE,
        FIXED,
        VEHICLE
    }
}
