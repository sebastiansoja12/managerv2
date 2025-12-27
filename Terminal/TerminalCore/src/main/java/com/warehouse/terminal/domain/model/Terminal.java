package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.enumeration.ExecutionSourceType;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.dto.DeviceValidationRequestDto;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;

import java.time.Instant;

public class Terminal extends Device implements DeviceHandler, ExecutionSourceResolver {
    private TerminalId terminalId;
    private DeviceType deviceType;
    private Username username;
    private String departmentCode;
    private String version;
    private Instant lastUpdate;
    private Boolean active;

	public Terminal(final TerminalId terminalId,
                    final DeviceType deviceType,
                    final Username username,
                    final String departmentCode,
                    final String version,
                    final Instant lastUpdate,
                    final Boolean active) {
        super(terminalId, version, deviceType, username);
        this.terminalId = terminalId;
        this.deviceType = deviceType;
        this.username = username;
        this.departmentCode = departmentCode;
        this.version = version;
        this.lastUpdate = lastUpdate;
        this.active = active;
    }

	public static Terminal from(final TerminalAddRequest request) {
		return new Terminal(null, request.getDeviceType(), request.getUsername(), request.getDepartmentCode(),
				request.getVersion(), Instant.now(), true);
	}

    public static Terminal from(final DeviceEntity device) {
        return new Terminal(new TerminalId(device.getDeviceId().value()), device.getDeviceType(), device.getUsername(),
                device.getDepartmentCode().getValue(), device.getVersion(),
                Instant.now(), device.isActive());
    }

    public static Terminal from(final DeviceValidationRequestDto request) {
        return new Terminal(new TerminalId(request.deviceId().value()), DeviceType.valueOf(request.deviceType().name()),
                new Username(request.username().value()), request.departmentCode().value(), request.version().value(),
                null, request.active());
    }

    public TerminalId getTerminalId() {
        return terminalId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Username getUsername() {
        return username;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getVersion() {
        return version;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void updateUserId(final Username username) {
        this.username = username;
        markAsModified();
    }

    public void updateDepartmentCode(final String departmentCode) {
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

    public void assignUser(final Username username) {
        this.username = username;
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
        return deviceType.equals(this.deviceType);
    }
}
