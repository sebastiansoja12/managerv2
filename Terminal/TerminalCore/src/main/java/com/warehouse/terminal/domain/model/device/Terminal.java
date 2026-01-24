package com.warehouse.terminal.domain.model.device;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.enumeration.ExecutionSourceType;
import com.warehouse.terminal.domain.model.DeviceHandler;
import com.warehouse.terminal.domain.model.ExecutionSourceResolver;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.vo.*;

public class Terminal extends Device implements DeviceHandler, ExecutionSourceResolver {
    private DeviceId deviceId;
    private DeviceType deviceType;
    private UserId userId;
    private DepartmentCode departmentCode;
    private String version;
    private Instant lastUpdate;
    private Boolean active;

    public Terminal(
            final DeviceId deviceId,
            final DeviceStatus status,
            final IdentityInfo identity,
            final HardwareProfile hardware,
            final SoftwareProfile software,
            final NetworkProfile network,
            final LocationProfile location,
            final OwnershipProfile ownership) {
		super(deviceId, ExternalId.generateId(), DeviceType.TERMINAL, status, identity, hardware, software, network, SecurityProfile.empty(), location,
				ownership);
	}

    public DeviceId getTerminalId() {
        return deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public UserId getUsername() {
        return userId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public String getVersion() {
        return version;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void updateUserId(final UserId userId) {
        this.userId = userId;
        markAsModified();
    }

    public void updateDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
        markAsModified();
    }

    public void updateDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
        markAsModified();
    }

    private void markAsModified() {
        this.lastUpdate = Instant.now();
    }

    public void updateVersion(final String version) {
        this.version = version;
        markAsModified();
    }

    public void assignUser(final UserId userId) {
        this.userId = userId;
        markAsModified();
    }

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
}
