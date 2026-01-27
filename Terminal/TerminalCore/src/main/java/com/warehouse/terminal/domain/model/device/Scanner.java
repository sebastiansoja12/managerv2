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
import com.warehouse.terminal.domain.vo.*;

public class Scanner implements Device, DeviceHandler, ExecutionSourceResolver {

    private DeviceId deviceId;
    private ExternalId<String> externalDeviceId;
    private DeviceType deviceType;
    private DeviceStatus status;
    private IdentityInfo identity;
    private HardwareProfile hardware;
    private NetworkProfile network;
    private OwnershipProfile ownership;
    private ScanType scanType;
    private ScannerType scannerType;
    private Instant createdAt;
    private Instant updatedAt;

    public Scanner(final DeviceId deviceId,
                   final IdentityInfo identity,
                   final HardwareProfile hardware,
                   final NetworkProfile network,
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
                   final IdentityInfo identity,
                   final HardwareProfile hardware,
                   final SoftwareProfile software,
                   final NetworkProfile network,
                   final SecurityProfile security,
                   final LocationProfile location,
                   final OwnershipProfile ownership,
                   final Instant createdAt,
                   final Instant updatedAt,
                   final UserId userId,
                   final DepartmentCode departmentCode,
                   final Boolean active,
                   final ScanType scanType,
                   final ScannerType scannerType) {
        this.deviceId = deviceId;
        this.externalDeviceId = externalDeviceId != null ? externalDeviceId : ExternalId.generateId();
        this.deviceType = deviceType != null ? deviceType : DeviceType.SCANNER;
        this.status = status != null ? status : (Boolean.TRUE.equals(active) ? DeviceStatus.ACTIVE : DeviceStatus.BLOCKED);
        this.identity = identity;
        this.hardware = hardware;
        this.network = network;
        this.ownership = ownership != null ? ownership : OwnershipProfile.initializeOwnership("", userId, departmentCode, null);
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
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
    public IdentityInfo getIdentity() {
        return identity;
    }

    @Override
    public HardwareProfile getHardware() {
        return hardware;
    }

    @Override
    public SoftwareProfile getSoftware() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NetworkProfile getNetwork() {
        return network;
    }

    @Override
    public SecurityProfile getSecurity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocationProfile getLocation() {
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
                getUserId(),
                getDepartmentCode(),
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
    public void updateUserId(final UserId userId) {
        ensureOwnership().setUserId(userId);
        markAsModified();
    }

    @Override
    public void updateDepartmentCode(final DepartmentCode departmentCode) {
        ensureOwnership().setDepartmentCode(departmentCode);
        markAsModified();
    }

    @Override
    public void updateStatus(final DeviceStatus status) {
        this.status = status;
        markAsModified();
    }

    @Override
    public void updateExternalDeviceId(final ExternalId<String> externalDeviceId) {
        this.externalDeviceId = externalDeviceId;
        markAsModified();
    }

    @Override
    public void updateCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void updateUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public void updateLastUpdate(final Instant lastUpdate) {
        this.updatedAt = lastUpdate;
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
    public void updateIdentity(final IdentityInfo identity) {
        if (identity != null) {
            if (this.identity == null) {
                this.identity = identity;
            } else {
                this.identity.update(identity);
            }
            markAsModified();
        }
    }

    @Override
    public void updateHardware(final HardwareProfile hardware) {
        if (hardware != null) {
            if (this.hardware == null) {
                this.hardware = hardware;
            } else {
                this.hardware.update(hardware);
            }
            markAsModified();
        }
    }

    @Override
    public void updateNetwork(final NetworkProfile network) {
        this.network.update(network);
    }

    @Override
    public void updateOwnership(final OwnershipProfile ownership) {
        if (ownership != null) {
            if (this.ownership == null) {
                this.ownership = ownership;
            } else {
                this.ownership.update(ownership);
            }
            markAsModified();
        }
    }

    @Override
    public void assignUser(final UserId userId) {
        updateUserId(userId);
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
