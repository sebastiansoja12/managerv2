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

public class Terminal implements Device, DeviceHandler, ExecutionSourceResolver {
    private DeviceId deviceId;
    private ExternalId<String> externalDeviceId;
    private DeviceType deviceType;
    private DeviceStatus status;
    private DeviceIdentity identity;
    private DeviceHardware hardware;
    private DeviceSoftware software;
    private DeviceNetwork network;
    private SecurityProfile security;
    private DeviceLocation location;
    private OwnershipProfile ownership;
    private Instant createdAt;
    private Instant updatedAt;

    private String version;
    private Instant lastUpdate;

    public Terminal(final DeviceId deviceId,
                    final DeviceIdentity identity,
                    final DeviceHardware hardware,
                    final DeviceSoftware software,
                    final DeviceNetwork network,
                    final DeviceLocation location,
                    final OwnershipProfile ownership) {
        this(deviceId,
                ExternalId.generateId(),
                DeviceType.TERMINAL,
                DeviceStatus.ACTIVE,
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
                Instant.now());
    }

    public Terminal(final DeviceId deviceId,
                    final ExternalId<String> externalDeviceId,
                    final DeviceType deviceType,
                    final DeviceStatus status,
                    final DeviceIdentity identity,
                    final DeviceHardware hardware,
                    final DeviceSoftware software,
                    final DeviceNetwork network,
                    final SecurityProfile security,
                    final DeviceLocation location,
                    final OwnershipProfile ownership,
                    final Instant createdAt,
                    final Instant updatedAt,
                    final String version,
                    final Instant lastUpdate) {
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
    public DeviceIdentity getIdentity() {
        return identity;
    }

    @Override
    public DeviceHardware getHardware() {
        return hardware;
    }

    @Override
    public DeviceSoftware getSoftware() {
        return software;
    }

    @Override
    public DeviceNetwork getNetwork() {
        return network;
    }

    @Override
    public SecurityProfile getSecurity() {
        return security;
    }

    @Override
    public DeviceLocation getLocation() {
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
                this.version,
                this.createdAt,
                this.updatedAt,
                this.lastUpdate,
                isActive(),
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
            this.status = DeviceStatus.ACTIVE;
        } else if (Boolean.FALSE.equals(active)) {
            this.status = DeviceStatus.BLOCKED;
        }
        markAsModified();
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
    public void updateSoftware(final DeviceSoftware software) {
        if (this.software == null) {
            this.software = software;
        } else {
            this.software.update(software);
        }
        this.version = this.software.getAppVersion();
        markAsModified();
    }

    @Override
    public void updateNetwork(final DeviceNetwork network) {
        this.network.update(network);
    }

    @Override
    public void updateSecurity(final SecurityProfile security) {
        if (this.security == null) {
            this.security = security;
        } else {
            this.security.update(security);
        }
        markAsModified();
    }

    @Override
    public void updateLocation(final DeviceLocation location) {
        if (this.location == null) {
            this.location = location;
        } else {
            this.location.update(location);
        }
        markAsModified();
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
            this.software = new DeviceSoftware();
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
