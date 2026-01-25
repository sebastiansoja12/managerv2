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

public class Terminal implements Device, DeviceHandler, ExecutionSourceResolver {
    private DeviceId deviceId;
    private ExternalId<String> externalDeviceId;
    private DeviceType deviceType;
    private DeviceStatus status;
    private IdentityInfo identity;
    private HardwareProfile hardware;
    private SoftwareProfile software;
    private NetworkProfile network;
    private SecurityProfile security;
    private LocationProfile location;
    private OwnershipProfile ownership;
    private Instant createdAt;
    private Instant updatedAt;

    private String version;
    private Instant lastUpdate;
    private Boolean active;

    public Terminal(final DeviceId deviceId,
                    final DeviceStatus status,
                    final IdentityInfo identity,
                    final HardwareProfile hardware,
                    final SoftwareProfile software,
                    final NetworkProfile network,
                    final LocationProfile location,
                    final OwnershipProfile ownership) {
        this(deviceId,
                ExternalId.generateId(),
                DeviceType.TERMINAL,
                status,
                identity,
                hardware,
                software,
                network,
                SecurityProfile.empty(),
                location,
                ownership,
                Instant.now(),
                Instant.now(),
                software != null ? software.getAppVersion() : null,
                Instant.now(),
                DeviceStatus.ACTIVE.equals(status));
    }

    public Terminal(final DeviceId deviceId,
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
                    final String version,
                    final Instant lastUpdate,
                    final Boolean active) {
        this.deviceId = deviceId;
        this.externalDeviceId = externalDeviceId;
        this.deviceType = deviceType;
        this.status = status;
        this.identity = identity;
        this.hardware = hardware;
        this.software = software;
        this.network = network;
        this.security = security;
        this.location = location;
        this.ownership = ownership;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
        this.lastUpdate = lastUpdate;
        this.active = active;
    }

    public DeviceId getTerminalId() {
        return deviceId;
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
        return software;
    }

    @Override
    public NetworkProfile getNetwork() {
        return network;
    }

    @Override
    public SecurityProfile getSecurity() {
        return security;
    }

    @Override
    public LocationProfile getLocation() {
        return location;
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
                this.software,
                this.network,
                this.security,
                this.location,
                this.ownership,
                getUserId(),
                getDepartmentCode(),
                this.version,
                this.createdAt,
                this.updatedAt,
                this.lastUpdate,
                this.active,
                null,
                null
        );
    }

    public UserId getUsername() {
        return getUserId();
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
        return version;
    }

    @Override
    public Instant getLastUpdate() {
        return lastUpdate;
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
        this.active = DeviceStatus.ACTIVE.equals(status);
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
        this.lastUpdate = lastUpdate;
        if (lastUpdate != null) {
            this.updatedAt = lastUpdate;
        }
    }

    @Override
    public void updateActive(final Boolean active) {
        this.active = active;
        if (Boolean.TRUE.equals(active)) {
            this.status = DeviceStatus.ACTIVE;
        } else if (Boolean.FALSE.equals(active)) {
            this.status = DeviceStatus.BLOCKED;
        }
        markAsModified();
    }

    @Override
    public void updateIdentity(final IdentityInfo identity) {
        this.identity = identity;
        markAsModified();
    }

    @Override
    public void updateHardware(final HardwareProfile hardware) {
        this.hardware = hardware;
        markAsModified();
    }

    @Override
    public void updateSoftware(final SoftwareProfile software) {
        this.software = software;
        this.version = software != null ? software.getAppVersion() : null;
        markAsModified();
    }

    @Override
    public void updateNetwork(final NetworkProfile network) {
        this.network = network;
        markAsModified();
    }

    @Override
    public void updateSecurity(final SecurityProfile security) {
        this.security = security;
        markAsModified();
    }

    @Override
    public void updateLocation(final LocationProfile location) {
        this.location = location;
        markAsModified();
    }

    @Override
    public void updateOwnership(final OwnershipProfile ownership) {
        this.ownership = ownership;
        markAsModified();
    }

    @Override
    public void updateDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
        markAsModified();
    }

    private void markAsModified() {
        this.lastUpdate = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Override
    public void updateVersion(final String version) {
        this.version = version;
        if (this.software == null) {
            this.software = new SoftwareProfile();
        }
        this.software.setAppVersion(version);
        markAsModified();
    }

    @Override
    public void assignUser(final UserId userId) {
        ensureOwnership().setUserId(userId);
        markAsModified();
    }

    @Override
    public Boolean isActive() {
        return active;
    }

    @Override
    public ExecutionSourceType getExecutionSourceType() {
        return ExecutionSourceType.DEVICE;
    }

    @Override
    public boolean canHandle(final DeviceType deviceType) {
        return DeviceType.TERMINAL.equals(deviceType);
    }

    private OwnershipProfile ensureOwnership() {
        if (this.ownership == null) {
            this.ownership = OwnershipProfile.initializeOwnership("", null, null, null);
        }
        return this.ownership;
    }
}
