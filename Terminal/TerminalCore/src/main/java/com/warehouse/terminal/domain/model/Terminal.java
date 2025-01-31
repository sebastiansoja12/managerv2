package com.warehouse.terminal.domain.model;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.terminal.domain.enumeration.ExecutionSourceType;
import com.warehouse.terminal.domain.model.request.TerminalAddRequest;
import com.warehouse.terminal.dto.DeviceValidationRequestDto;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DeviceEntity;

import java.time.Instant;

public class Terminal extends Device implements ExecutionSourceResolver {
    private TerminalId terminalId;
    private DeviceType deviceType;
    private UserId userId;
    private String departmentCode;
    private String version;
    private Instant lastUpdate;
    private Boolean active;

	public Terminal(final TerminalId terminalId,
                    final DeviceType deviceType,
                    final UserId userId,
                    final String departmentCode,
                    final String version,
                    final Instant lastUpdate, 
                    final Boolean active) {
        super(terminalId, version, deviceType, userId);
        this.terminalId = terminalId;
        this.deviceType = deviceType;
        this.userId = userId;
        this.departmentCode = departmentCode;
        this.version = version;
        this.lastUpdate = lastUpdate;
        this.active = active;
    }

	public static Terminal from(final TerminalAddRequest request, final UserId userId) {
		return new Terminal(null, request.getDeviceType(), userId, request.getDepartmentCode(), request.getVersion(),
				Instant.now(), true);
	}

    public static Terminal from(final DeviceEntity device) {
        return new Terminal(new TerminalId(device.getDeviceId().getValue()), device.getDeviceType(), device.getUserId(),
                device.getDepartmentCode().getValue(), device.getVersion(),
                Instant.now(), device.isActive());
    }

    public static Terminal from(final DeviceValidationRequestDto request) {
        return new Terminal(new TerminalId(request.deviceId().value()), DeviceType.valueOf(request.deviceType().name()),
                new UserId(request.userId().value()), request.departmentCode().value(), request.version().value(),
                null, request.active());
    }

    public TerminalId getTerminalId() {
        return terminalId;
    }

    public DeviceType getDeviceType() {
        return DeviceType.TERMINAL;
    }

    public UserId getUserId() {
        return userId;
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

    public void updateUserId(final UserId userId) {
        this.userId = userId;
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
}
