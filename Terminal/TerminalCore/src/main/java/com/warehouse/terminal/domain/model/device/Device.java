package com.warehouse.terminal.domain.model.device;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.enumeration.ExecutionSourceType;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.vo.*;

public class Device {

    private DeviceId deviceId;
    private ExternalId<String> externalDeviceId;

    private DeviceType type;
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

    public Device(
            final DeviceId deviceId,
            final ExternalId<String> externalDeviceId,
            final DeviceType type,
            final DeviceStatus status,
            final IdentityInfo identity,
            final HardwareProfile hardware,
            final SoftwareProfile software,
            final NetworkProfile network,
            final SecurityProfile security,
            final LocationProfile location,
            final OwnershipProfile ownership
    ) {
        this.deviceId = deviceId;
        this.externalDeviceId = externalDeviceId;
        this.type = type;
        this.status = status;
        this.identity = identity;
        this.hardware = hardware;
        this.software = software;
        this.network = network;
        this.security = security;
        this.location = location;
        this.ownership = ownership;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public ExternalId<String> getExternalDeviceId() {
        return externalDeviceId;
    }

    public HardwareProfile getHardware() {
        return hardware;
    }

    public IdentityInfo getIdentity() {
        return identity;
    }

    public LocationProfile getLocation() {
        return location;
    }

    public NetworkProfile getNetwork() {
        return network;
    }

    public OwnershipProfile getOwnership() {
        return ownership;
    }

    public SecurityProfile getSecurity() {
        return security;
    }

    public SoftwareProfile getSoftware() {
        return software;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getType() {
        return ExecutionSourceType.DEVICE.name();
    }

    public void assignUser(final UserId userId) {
        this.ownership.setUserId(userId);
        markAsModified();
    }

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }

    public DeviceType getDeviceType() {
        return type;
    }

    public void updateDeviceType(final DeviceType deviceType) {
    }
}

