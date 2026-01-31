package com.warehouse.terminal.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.ExecutionSourceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.vo.*;

public interface Device {

    DeviceId getDeviceId();

    ExternalId<String> getExternalDeviceId();

    DeviceType getDeviceType();

    DeviceStatus getStatus();

    IdentityInfo getIdentity();

    HardwareProfile getHardware();

    SoftwareProfile getSoftware();

    NetworkProfile getNetwork();

    SecurityProfile getSecurity();

    LocationProfile getLocation();

    OwnershipProfile getOwnership();

    Instant getCreatedAt();

    Instant getUpdatedAt();

    DeviceSnapshot toSnapshot();

    default UserId getUserId() {
        throw new UnsupportedOperationException();
    }

    default DepartmentCode getDepartmentCode() {
        throw new UnsupportedOperationException();
    }

    default String getVersion() {
        throw new UnsupportedOperationException();
    }

    default Instant getLastUpdate() {
        return getUpdatedAt();
    }

    default Boolean isActive() {
        return DeviceStatus.ACTIVE.equals(getStatus());
    }

    default String getType() {
        return ExecutionSourceType.DEVICE.name();
    }

    void assignUser(UserId userId);

    default void updateUserId(final UserId userId) {
        throw new UnsupportedOperationException();
    }

    default void updateDepartmentCode(final DepartmentCode departmentCode) {
        throw new UnsupportedOperationException();
    }

    default void updateVersion(final String version) {
        throw new UnsupportedOperationException();
    }

    default void updateExternalDeviceId(final ExternalId<String> externalDeviceId) {
        throw new UnsupportedOperationException();
    }

    default void updateStatus(final DeviceStatus status) {
        throw new UnsupportedOperationException();
    }

    default void updateCreatedAt(final Instant createdAt) {
        throw new UnsupportedOperationException();
    }

    default void updateUpdatedAt(final Instant updatedAt) {
        throw new UnsupportedOperationException();
    }

    default void updateLastUpdate(final Instant lastUpdate) {
        throw new UnsupportedOperationException();
    }

    default void updateActive(final Boolean active) {
        throw new UnsupportedOperationException();
    }

    default void updateIdentity(final IdentityInfo identity) {
        throw new UnsupportedOperationException();
    }

    default void updateHardware(final HardwareProfile hardware) {
        throw new UnsupportedOperationException();
    }

    default void updateSoftware(final SoftwareProfile software) {
        throw new UnsupportedOperationException();
    }

    default void updateNetwork(final NetworkProfile network) {
        throw new UnsupportedOperationException();
    }

    default void updateSecurity(final SecurityProfile security) {
        throw new UnsupportedOperationException();
    }

    default void updateLocation(final LocationProfile location) {
        throw new UnsupportedOperationException();
    }

    default void updateOwnership(final OwnershipProfile ownership) {
        throw new UnsupportedOperationException();
    }

    void updateDeviceType(DeviceType deviceType);

    default void changeVersion(final String version) {
        updateVersion(version);
    }
}
