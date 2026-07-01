package com.warehouse.terminal.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.ExecutionSourceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.command.DeviceUpdateCommand;
import com.warehouse.terminal.domain.vo.*;

public interface Device {

    DeviceId getDeviceId();

    ExternalId<String> getExternalDeviceId();

    DeviceType getDeviceType();

    DeviceStatus getStatus();

    DeviceIdentity getIdentity();

    DeviceHardware getHardware();

    DeviceSoftware getSoftware();

    DeviceNetwork getNetwork();

    SecurityProfile getSecurity();

    DeviceLocation getLocation();

    OwnershipProfile getOwnership();

    Instant getCreatedAt();

    Instant getUpdatedAt();

    DeviceSnapshot toSnapshot();

    UserId getUserId();

    DepartmentCode getDepartmentCode();

    String getVersion();

    Instant getLastUpdate();

    default Boolean isActive() {
        return DeviceStatus.ACTIVE.equals(getStatus());
    }

    default String getType() {
        return ExecutionSourceType.DEVICE.name();
    }

    void assignUser(UserId userId);

    void assignDepartmentCode(final DepartmentCode departmentCode);

    default void updateVersion(final String version) {
        throw new UnsupportedOperationException();
    }

    void updateStatus(final DeviceStatus status);

    void updateActive(final Boolean active);

    void updateIdentity(final DeviceIdentity identity);

    void updateHardware(final DeviceHardware hardware);

    default void updateSoftware(final DeviceSoftware software) {
        throw new UnsupportedOperationException();
    }

    void updateNetwork(final DeviceNetwork network);

    default void updateSecurity(final SecurityProfile security) {
        throw new UnsupportedOperationException();
    }

    default void updateLocation(final DeviceLocation location) {
        throw new UnsupportedOperationException();
    }

    void updateOwnership(final OwnershipProfile ownership);

    void updateDeviceType(final DeviceType deviceType);

    void update(final DeviceUpdateCommand request);

    default void changeVersion(final String version) {
        updateVersion(version);
    }
}
